/*
 * Copyright 2014 agwlvssainokuni
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cherry.foundation.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessagePostProcessor;

import cherry.foundation.bizdtm.BizDateTime;
import cherry.goods.command.CommandLauncher;
import cherry.goods.command.CommandResult;

/**
 * 非同期実行フレームワーク。<br />
 * 非同期にコマンドを実行する機能を提供する。
 */
public class AsyncCommandHandlerImpl implements AsyncCommandHandler {

	private static final String ASYNCID = "asyncId";
	private static final String COMMAND = "command";

	private BizDateTime bizDateTime;

	private AsyncProcessStore asyncProcessStore;

	private JmsOperations jmsOperations;

	private CommandLauncher commandLauncher;

	private MessagePostProcessor messagePostProcessor;

	public void setBizDateTime(BizDateTime bizDateTime) {
		this.bizDateTime = bizDateTime;
	}

	public void setAsyncProcessStore(AsyncProcessStore asyncProcessStore) {
		this.asyncProcessStore = asyncProcessStore;
	}

	public void setJmsOperations(JmsOperations jmsOperations) {
		this.jmsOperations = jmsOperations;
	}

	public void setCommandLauncher(CommandLauncher commandLauncher) {
		this.commandLauncher = commandLauncher;
	}

	public void setMessagePostProcessor(
			MessagePostProcessor messagePostProcessor) {
		this.messagePostProcessor = messagePostProcessor;
	}

	/**
	 * 非同期のコマンド実行を実行登録する。
	 *
	 * @param launcherId
	 *            非同期処理の実行者のID。
	 * @param description
	 *            内容表記。
	 * @param command
	 *            実行するコマンド。
	 * @param args
	 *            引数。
	 * @return 非同期実行状況の管理データのID。
	 */
	@Override
	public long launchCommand(String launcherId, String description,
			String command, String... args) {

		long asyncId = asyncProcessStore.createCommand(launcherId,
				bizDateTime.now(), description, command, args);

		Map<String, String> message = new HashMap<>();
		message.put(ASYNCID, String.valueOf(asyncId));
		message.put(COMMAND, command);
		for (int i = 0; i < args.length; i++) {
			message.put(String.valueOf(i), args[i]);
		}
		jmsOperations.convertAndSend(message, messagePostProcessor);

		asyncProcessStore.updateToLaunched(asyncId, bizDateTime.now());
		return asyncId;
	}

	/**
	 * 実行登録したコマンドを実行する。<br />
	 * 本メソッドはコンテナが呼出すことを意図するものである。
	 *
	 * @param message
	 *            {@link #launchCommand(String, String...)}
	 *            において登録した内容がコンテナから受渡される。
	 */
	@Override
	public void handleMessage(Map<String, String> message) {

		long asyncId = Long.parseLong(message.get(ASYNCID));
		List<String> cmdline = new ArrayList<>();
		cmdline.add(message.get(COMMAND));
		for (int i = 0;; i++) {
			String v = message.get(String.valueOf(i));
			if (v == null) {
				break;
			}
			cmdline.add(v);
		}

		asyncProcessStore.updateToProcessing(asyncId, bizDateTime.now());
		try {

			CommandResult result = commandLauncher.launch(cmdline
					.toArray(new String[cmdline.size()]));

			AsyncStatus status;
			if (result.getExitValue() == 0) {
				status = AsyncStatus.SUCCESS;
			} else {
				status = AsyncStatus.ERROR;
			}

			asyncProcessStore.finishCommand(asyncId, bizDateTime.now(), status,
					result);
		} catch (Exception ex) {
			asyncProcessStore.finishWithException(asyncId, bizDateTime.now(),
					ex);
		}
	}

}
