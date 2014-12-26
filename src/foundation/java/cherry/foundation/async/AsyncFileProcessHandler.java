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

import java.util.Map;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.multipart.MultipartFile;

/**
 * 非同期実行フレームワーク。<br />
 * 非同期にファイル処理を実行する仕組みを提供する。ファイル処理の実体は業務ロジックごとに{@link FileProcessHandler}
 * を実装することとする。
 */
public interface AsyncFileProcessHandler {

	/**
	 * 非同期のファイル処理を実行登録する。
	 *
	 * @param launcherId
	 *            非同期処理の実行者のID。
	 * @param description
	 *            内容表記。
	 * @param file
	 *            処理対象のファイル。
	 * @param handlerName
	 *            非同期のファイル処理の処理を実装したBeanの名前。同Beanは{@link FileProcessHandler}
	 *            を実装しなければならない。
	 * @param args
	 *            引数。
	 * @return 非同期実行状況の管理データのID。
	 */
	long launchFileProcess(String launcherId, String description,
			MultipartFile file, String handlerName, String... args);

	/**
	 * 実行登録したファイル処理を実行する。<br />
	 * 本メソッドはコンテナが呼出すことを意図するものであり、{@link JmsListener}アノテーションを付与する。
	 *
	 * @param message
	 *            {@link #launchFileProcess(String, MultipartFile, String)}
	 *            において登録した内容がコンテナから受渡される。
	 */
	void handleMessage(Map<String, String> message);

}
