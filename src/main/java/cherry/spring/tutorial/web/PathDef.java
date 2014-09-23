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

package cherry.spring.tutorial.web;

/**
 * URIパス、VIEWパスを定義します。
 */
public class PathDef {

	/** URI: ログイン画面 */
	public static final String URI_LOGIN = "/login";

	/** ビュー: ログイン画面 */
	public static final String VIEW_LOGIN = "login/init";

	/** URI: トップ画面 */
	public static final String URI_HOME = "/secure/";

	/** ビュー: トップ画面 */
	public static final String VIEW_HOME = "secure/home/init";

	/** URI: TODO登録画面 */
	public static final String URI_TODO_CREATE = "/secure/todo/create";

	/** ビュー: TODO登録画面 */
	public static final String VIEW_TODO_CREATE = "secure/todo/create/init";

	/** ビュー: TODO登録確認画面 */
	public static final String VIEW_TODO_CREATE_CONFIRM = "secure/todo/create/confirm";

	/** ビュー: TODO登録完了画面 */
	public static final String VIEW_TODO_CREATE_FINISH = "secure/todo/create/finish";

	/** URI: TODO編集画面 */
	public static final String URI_TODO_EDIT = "/secure/todo/{id}";

	/** ビュー: TODO編集画面 */
	public static final String VIEW_TODO_EDIT = "secure/todo/edit/init";

	/** ビュー: TODO編集確認画面 */
	public static final String VIEW_TODO_EDIT_CONFIRM = "secure/todo/edit/confirm";

	/** ビュー: TODO編集完了画面 */
	public static final String VIEW_TODO_EDIT_FINISH = "secure/todo/edit/finish";

	/** URI: TODO検索画面 */
	public static final String URI_TODO_LIST = "/secure/todo/list";

	/** ビュー: TODO検索画面 */
	public static final String VIEW_TODO_LIST = "/secure/todo/list/init";

	/** URIサブパス: 確認 */
	public static final String SUBURI_CONFIRM = "confirm";

	/** URIサブパス: 実行 */
	public static final String SUBURI_EXECUTE = "execute";

	/** URIサブパス: 完了 */
	public static final String SUBURI_FINISH = "finish";

	/** URIパス変数名 */
	public static final String PATH_VAR_ID = "id";

}
