Spring MVCチュートリアル(2)
=========================

# 概要
*	STEP 10: TODO編集画面を作成する。(1)
*	STEP 11: TODO編集画面を作成する。(2)
*	STEP 12: TODO編集画面を作成する。(3)
*	STEP 13: TODO検索画面を作成する。(1)
*	STEP 14: TODO検索画面を作成する。(2)
*	STEP 15: TODO検索画面を作成する。(3)
*	STEP 16: TODO検索画面を作成する。(4)


# STEP 10: TODO編集画面を作成する。(1)

次に、TODO編集画面を作成します。

## 画面の仕様

画面の仕様を確認します。TODO編集は「編集画面」のみで構成されます。「編集画面」で変更内容を記入し、「変更」ボタンを押下するとすぐに変更がDBに反映され、変更後の内容で「編集画面」が表示されます。

*	外部仕様
	*	編集画面
		*	URIパス: `/secure/todo/{id}`
			*	TODOレコードの主キー値(`id`列の値)を「パス変数」で指定する。
		*	入力フォーム
			*	TODOの期日のパラメタ名: `dueDate`
			*	TODOの内容のパラメタ名: `description`
			*	TODOの完了フラグのパラメタ名: `doneFlg`
			*	(表示のみ) TODOの完了日時: `doneAt`
			*	(隠し項目) TODOのロックバージョン(楽観排他制御項目): `lockVersion`
		*	「変更」ボタンを押下すると、「変更処理」へPOSTする。
	*	変更処理
		*	URIパス: `/secure/todo/{id}/execute`
			*	TODOレコードの主キー値(`id`列の値)を「パス変数」で指定する。
		*	POSTされた値でDBを更新し、「編集画面」へHTTPリダイレクトする(画面遷移する)。
*	内部仕様
	*	共通
		*	フォーム: `cherry.spring.tutorial.web.secure.todo.edit.TodoEditForm`
		*	コントローラ
			*	インタフェース: `cherry.spring.tutorial.web.secure.todo.list.TodoEditController`
			*	実装クラス: `cherry.spring.tutorial.web.secure.todo.list.TodoEditControllerImpl`
	*	編集画面
		*	メソッド: `init`
			*	`ModelAndView`を作りビュー名を設定する。
			*	`ModelAndView`を返却する。
		*	メソッド: `getForm`
			*	パス変数に指定された主キー値を持つTODOレコードを照会する。
			*	画面に表示するフォームを作成し、TODOレコードが保持するデータを詰替える。
			*	フォームを返却する。
		*	JSP: `/WEB-INF/tiles/secure/todo/edit/init.jsp` (ビュー名: `secure/todo/edit/init`)
	*	変更処理
		*	メソッド: `execute`
			*	パス変数に指定された主キー値を持つTODOレコードを照会する。
			*	引数として受渡されたフォームとバインド結果から入力値の妥当性を検証する。
			*	多重POSTを検証する。(ワンタイムトークン方式)
			*	入力値をフォームから取得し、DBのTODOレコードを更新する。
			*	楽観排他エラーを検証する。
			*	`ModelAndView`を作りリダイレクト先のURIパスを設定する。TODOレコードの主キー(`id`)はURIパスに埋込まれる。
			*	形成した`ModelAndView`を返却する。
		*	編集画面へHTTPリダイレクトする。

## 技術要素
TODO編集画面では、TODO登録画面で取り上げられなかった、下記2つの技術要素を中心に説明します。

*	パス変数
*	楽観ロック (楽観排他)


### パス変数
WEBアプリケーションにおける入力パラメタの引渡し方で、URIパスに埋込んでパラメタを引渡すことを、「パス変数」による引渡しと呼びます。昔ながらのWEBアプリケーションでは、クエリストリングに「`.../foo?id=101`」のように指定しました。パス変数を使うと「`.../foo/101`」のように表現します。
この記法が一般的になったのは、REST(Representational State Transfer)という概念が知られるようになり、RESTのAPIでは主キー値をURIパスに埋込むのが標準的だったことが影響していると思われます。
モダンなWEBアプリケーションフレームワークは、パス変数によるパラメタ引渡しに対応しています。

Spring MVCでは以下の2点によりパス変数を使ったパラメタ引渡しができるようになります。

*	`@RequestMapping()`アノテーションに指定するURIパスに「`{変数名}`」の形式で変数名を組込む。
*	同アノテーションを付与したメソッドの引数に`@PathVariable("変数名")`アノテーションを付与すると、機能を実行する時にその引数に該当するパタメタ値が引渡されてメソッドが呼出される。

### 楽観ロック (楽観排他)
WEBアプリケーションにおいてデータを変更する画面を作る場合の典型的な流れを以下に示します。

*	対象のデータを識別するキーを渡して「現在のデータ値」を編集画面に表示する。
*	利用者は、編集画面において現在のデータ値を見ながら、「新しいデータ値」を同画面に入力する。
*	利用者が、編集画面で変更ボタンを押下すると、サーバの「変更処理」が呼出される。
*	サーバで変更処理が実行される。

この流れで編集画面の表示と変更処理は異なるHTTPリクエストに対して実行されます。編集画面にデータを表示してから変更処理を実行するまでデータをロックしておくことはできません。即ち、「第一項でデータを表示してから第四項でデータを変更するまでの間に、他の誰かが同じデータを変更していた場合、後から変更を試みた側はどう捌くべきか」という課題が顕在化します。捌き方の選択肢は「後から変更を試みた側は『自分の変更で上書きする』」(後勝ち)、または、「後から変更を試みた側は『自分の変更を断念する』」(先勝ち)のいずれかです。
特に制御しなければ前者の動作です。
「楽観ロック(楽観排他)」の制御をすることで後者の動作をさせることができます。

楽観ロックは、「編集画面に表示した時点のデータの『バージョン』を記録しておき、変更処理を実行する時にDBMS上の当該データの『バージョン』と照合することで、他者が変更したか否かを判定する(= バージョン値が同じならば他者は変更していない、バージョン値が異なれば他者が変更した)」という方式です。データを変更する全ての処理が「変更する時はバージョン値を照合するとともに、バージョン値も変更する」という規則に則ることが前提です。「皆同じように制御するはずだから概ねうまくいくだろう」と文字通り楽観的に捉える方式です。
バージョンは数値やタイムスタンプが使われます。
本チュートリアルでは数値をバージョン(`lock_version`列)として使用します。

## 実装の仕方
TODO登録画面と同じように、何をしているか理解しながら進めることを意図し、少しずつ順を追って実装していきます。ただし、これまでに説明していない技術要素に注目します。既出の技術要素については説明を省略して少しスピードを上げます。
実装は下記の3つの段階に分けて進めていきます。

*	基本的な画面遷移を実装する。
*	入力および妥当性検証NGの画面遷移を作成する。
*	TODO編集画面の主たる業務ロジックである「DBのTODOレコードを更新する」を実装する。

STEP 10では「基本的な画面遷移を実装」します。

## フォーム
まずはじめにフォームを定義します。
TODO登録画面の「期日」「内容」に加えて、「完了フラグ」がTODO編集画面の入力項目です。利用者の操作によって「入力」されるものがこれらの3項目です。この他に、表示のみの項目として「完了日時」を追加します。また、画面には表示しませんが「変更処理」に受渡す項目として「ロックバージョン」を追加します。
完了日時については、表示のみを目的とする項目ですので、必ずしもフォームのプロパティとして定義する必然性はありません。「他の項目と並べて表示するので一緒に定義すると使いやすい」のと「アノテーションを指定して表示の書式を能動的に制御する」の2点の理由で、フォームのプロパティとして定義することとします。
ロックバージョンは楽観ロック制御に使用する項目です。利用者に入力してもらうものではなく、また、利用者に見てもらうものでもありません。人が入力する値とアプリケーションが制御のために引回す値とで人の目から見ると位置づけの違いはありますが、プログラムから見るとその位置づけの違いは関係ありません。同じフォームのプロパティとして定義します。

下記のようにフォームを作成します。

```Java:TodoEditForm
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class TodoEditForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@CustomDateTimeFormat()
	private LocalDate dueDate;

	@NotEmpty
	@MaxLength(5000)
	private String description;

	private boolean doneFlg;

	@CustomDateTimeFormat()
	private LocalDateTime doneAt;

	private int lockVersion;

}
```

## コントローラ
コントローラ定義のポイントは「パス変数」です。
パス変数に対応するには、`@RequestMapping`アノテーションと`@PathVariable`アノテーションの両方を使用します。具体的には下記の通りです。

*	クラス(インタフェース)に`@RequestMapping("/secure/todo/{id}")`アノテーションを指定する。
*	メソッドの引数に`@PathVariable("id") int id`を指定してURIパスの`{id}`の部位の値を受取る。

URIパス、パス変数名は定数定義を指定するため、プログラムの字面からは分かりにくいですが、上記のように指定するということを意識してください。

### インタフェース
下記のようにコントローラのインタフェースを作成します。

```Java:TodoEditController
@RequestMapping(PathDef.URI_TODO_EDIT)
public interface TodoEditController {

	@ModelAttribute()
	TodoEditForm getForm(@PathVariable(PathDef.PATH_VAR_ID) int id,
			Authentication auth);

	@RequestMapping()
	ModelAndView init(@PathVariable(PathDef.PATH_VAR_ID) int id,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request);

	@RequestMapping(PathDef.SUBURI_EXECUTE)
	ModelAndView execute(@PathVariable(PathDef.PATH_VAR_ID) int id,
			@Validated TodoEditForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request, RedirectAttributes redirAttr);

}
```

### 実装クラス
STEP 10で、編集画面を表示するためのプログラム(`getForm()`メソッドと`init()`メソッド)を完成します。

TODO登録画面では、初期表示するフォーム(`TodoCreateForm`)を返却するメソッド`getForm()`を別に設け、`init()`メソッドではビューの名前を返却するだけでした。
TODO編集画面では、初期表示するフォーム(`TodoEditForm`)の値は「パス変数で指定された主キーを持つTODOレコード」です。パス変数の値に依存してフォームを作成する必要がありますので、`getForm()`メソッドもパス変数を受取るように構成します。TODOレコードの照会は、TODO登録画面の「完了画面」を表示するために作成したものを使います。取得したレコードの値を`TodoEditForm`に詰め直し、これを`getForm()`メソッドの返却値とします。

また、TODOレコードを照会した結果は`Contract.shouldExist()`メソッドで存在保証します。
Todoオブジェクトが存在しない(== null)場合は`NotFoundException`がthrowされ、後続の処理がスキップされます。また`NotFoundException`は別途定義済みの`@ExceptionHandler`が拾い、HTTPステータス404 (Not Found)として返却されます。

なお、`init()`メソッドでは、引数として受取ったパス変数の値を`ModelAndView`に格納してJSPに受渡します。URIパスから抽出されたパス変数の値は、そのままではJSPには渡されないため、コントローラのメソッドの中で明示的に受渡します。

下記のようにコントローラの実装クラスを作成します。

```Java:TodoEditControllerImpl
@Controller
public class TodoEditControllerImpl implements TodoEditController {

	@Autowired
	private TodoService todoService;

	@Autowired
	private BizdateHelper bizdateHelper;

	@Autowired
	private LogicalErrorHelper logicalErrorHelper;

	@Autowired
	private OneTimeTokenValidator oneTimeTokenValidator;

	@Override
	public TodoEditForm getForm(int id, Authentication auth) {

		Todo todo = todoService.findById(auth.getName(), id);
		Contract.shouldExist(todo, Todo.class, auth.getName(), id);

		TodoEditForm form = new TodoEditForm();
		form.setDueDate(todo.getDueDate());
		form.setDescription(todo.getDescription());
		form.setDoneFlg(todo.getDoneFlg() == FlagCode.TRUE);
		if (todo.getDoneFlg() == FlagCode.TRUE) {
			form.setDoneAt(todo.getDoneAt());
		}
		form.setLockVersion(todo.getLockVersion());
		return form;
	}

	@Override
	public ModelAndView init(int id, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
		mav.addObject(PathDef.PATH_VAR_ID, id);
		return mav;
	}

	@Override
	public ModelAndView execute(int id, TodoEditForm form,
			BindingResult binding, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request,
			RedirectAttributes redirAttr) {

		Todo todo = todoService.findById(auth.getName(), id);
		Contract.shouldExist(todo, Todo.class, auth.getName(), id);

		UriComponents uc = MvcUriComponentsBuilder.fromMethodName(
				TodoEditController.class, PathDef.METHOD_INIT, id, auth,
				locale, sitePref, request).build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(uc.toUriString(), true));
		return mav;
	}

}
```

## JSP
STEP 10では画面は最小限とします。下記のように作成します。

```HTML:/WEB-INF/tiles/secure/todo/edit/init.jsp
<h2>TODO編集</h2>
```


# STEP 11: TODO編集画面を作成する。(2)

STEP 11では「画面の入力および妥当性検証NGの画面遷移を作成」します。

## コントローラ
妥当性検証NGの判定は下記の通りです。

*	入力値の単項目チェック(字面上の様式検証)
	*	NG => 編集画面に入力エラーメッセージを表示する。
*	多重POSTチェック(ワンタイムトークンを照合する)
	*	NG => 編集画面に多重POSTエラーメッセージを表示する。

また、正常に処理すると編集画面にHTTPリダイレクトし、更新した旨のメッセージ(正常メッセージ)を表示します。

以上を踏まえ、下記のようにコントローラを作成します。

### 実装クラス

```Java:TodoEditControllerImpl
	@Override
	public ModelAndView execute(int id, TodoEditForm form,
			BindingResult binding, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request,
			RedirectAttributes redirAttr) {

		Todo todo = todoService.findById(auth.getName(), id);
		Contract.shouldExist(todo, Todo.class, auth.getName(), id);

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
			mav.addObject(PathDef.PATH_VAR_ID, id);
			return mav;
		}

		if (!oneTimeTokenValidator.isValid(request)) {
			logicalErrorHelper.rejectOnOneTimeTokenError(binding);
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
			mav.addObject(PathDef.PATH_VAR_ID, id);
			return mav;
		}

		redirAttr.addFlashAttribute("updated", true);

		UriComponents uc = MvcUriComponentsBuilder.fromMethodName(
				TodoEditController.class, PathDef.METHOD_INIT, id, auth,
				locale, sitePref, request).build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(uc.toUriString(), true));
		return mav;
	}
```

## JSP
JSPの作成項目は下記の通りです。

*	正常系メッセージ
	*	更新した旨のメッセージ
*	エラー系メッセージ
	*	入力エラーメッセージ(単項目チェック)
	*	多重POSTエラーメッセージ(ワンタイムトークン照合)
*	入力フォーム
	*	期日
	*	完了フラグ
	*	(表示のみ) 完了日時
	*	内容
	*	(隠し項目) ロックバージョン
	*	(隠し項目) ワンタイムトークン

以上を踏まえ、下記のようにJSPを作成します。

```HTML:/WEB-INF/tiles/secure/todo/edit/init.jsp
<h2>TODO編集</h2>
<c:if test="${updated}">
	<div class="has-success">
		<div class="help-block">TODOを更新しました。</div>
	</div>
</c:if>
<s:hasBindErrors name="todoEditForm">
	<div class="form-group has-error">
		<div class="help-block bg-danger">
			<f:errors path="todoEditForm" element="div" />
			<s:nestedPath path="todoEditForm">
				<f:errors path="dueDate" element="div" />
				<f:errors path="doneFlg" element="div" />
				<f:errors path="description" element="div" />
				<f:errors path="lockVersion" element="div" />
			</s:nestedPath>
		</div>
	</div>
</s:hasBindErrors>
<f:form servletRelativeAction="/secure/todo/${id}/execute" method="POST"
	modelAttribute="todoEditForm" role="form">
	<div class="form-group">
		<f:label path="dueDate" cssErrorClass="has-error">期日</f:label>
		<f:input path="dueDate" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<div class="form-group">
		<f:checkbox path="doneFlg" cssErrorClass="has-error" label="完了" />
	</div>
	<div class="form-group">
		<f:label path="doneAt">完了日時</f:label>
		<f:input path="doneAt" cssClass="form-control" readonly="true" />
	</div>
	<div class="form-group">
		<f:label path="description" cssErrorClass="has-error">内容</f:label>
		<f:textarea path="description" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<f:hidden path="lockVersion" />
	<f:button type="submit" class="btn btn-default">更新</f:button>
	<mytag:onetimetoken />
</f:form>
```

## message/form.propertiesファイル (項目の表示名)

```Ini:message/form.properties
todoEditForm.dueDate=期日
todoEditForm.description=内容
todoEditForm.doneFlg=完了フラグ
todoEditForm.lockVersion=ロックバージョン
```


# STEP 12: TODO編集画面を作成する。(3)

STEP 12では「TODO編集画面の主たる業務ロジックである「DBのTODOレコードを更新する」を実装」します。

## コントローラ
コントローラは下記の要領で処理を進めます。

*	フォームに受渡されたデータ(入力値＆隠し項目)をTodoインスタンスに詰替える。
	*	「完了日時」については、現状のデータが未完了で入力値が完了の場合に、業務日時をセットする。
*	サービス`TodoService`の更新メソッド(後述)を呼出す。
*	更新メソッドの結果を判定し、偽(= 楽観ロックエラー)ならば、編集画面に楽観ロックエラーである旨のメッセージを表示する。

以上を踏まえ、下記のようにコントローラを作成します。

### 実装クラス

```Java:TodoEditControllerImpl
	@Override
	public ModelAndView execute(int id, TodoEditForm form,
			BindingResult binding, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request,
			RedirectAttributes redirAttr) {

		Todo todo = todoService.findById(auth.getName(), id);
		Contract.shouldExist(todo, Todo.class, auth.getName(), id);

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
			mav.addObject(PathDef.PATH_VAR_ID, id);
			return mav;
		}

		if (!oneTimeTokenValidator.isValid(request)) {
			logicalErrorHelper.rejectOnOneTimeTokenError(binding);
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
			mav.addObject(PathDef.PATH_VAR_ID, id);
			return mav;
		}

		Todo newTodo = new Todo();
		newTodo.setDueDate(form.getDueDate());
		newTodo.setDescription(form.getDescription());
		newTodo.setDoneFlg(FlagCode.valueOf(form.isDoneFlg()));
		if (form.isDoneFlg() && !todo.getDoneFlg().isTrue()) {
			newTodo.setDoneAt(bizdateHelper.now());
		}
		newTodo.setLockVersion(form.getLockVersion());

		boolean result = todoService.update(auth.getName(), id, newTodo);
		if (!result) {
			logicalErrorHelper.rejectOnOptimisticLockError(binding);
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
			mav.addObject(PathDef.PATH_VAR_ID, id);
			return mav;
		}

		redirAttr.addFlashAttribute("updated", true);

		UriComponents uc = MvcUriComponentsBuilder.fromMethodName(
				TodoEditController.class, PathDef.METHOD_INIT, id, auth,
				locale, sitePref, request).build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(uc.toUriString(), true));
		return mav;
	}
```

## サービス
サービス`TodoService`に更新処理を`update`メソッドとして作成します。

更新処理の内容は下記のSQLです。

```SQL
UPDATE todo
SET
	due_date = {入力値:期日},
	description = {入力値:内容},
	done_flg = {入力値:完了フラグ},
	done_at = {業務日時　※完了する時のみSETする},
	lock_version = {隠し項目:ロックバージョン} + 1
WHERE
	id = {パス変数:id}
	AND
	posted_by = {ログインID}
	AND
	deleted_flg = 0
	AND
	lock_version = {隠し項目:ロックバージョン}
```

これをMyBatisで記述します。
具体的には、WHERE句を`TodoCriteria`インスタンスで指定します。
また、SET句を`Todo`インスタンスで指定します。渡された`Todo`インスタンスをSET句として指定することもできますが、ロックバージョンを「+1」するためには、引数に渡された`Todo`インスタンスを書替えなければなりません。これを許容するか否かはプロジェクトの方針に依存します。本チュートリアルでは新しいインスタンスを作成しています。

以上を踏まえ、下記のようにサービスを作成します。

### インタフェース

```Java:TodoService
	boolean update(String loginId, int id, Todo todo);
```

### 実装クラス

```Java:TodoServiceImpl
	@Transactional
	@Override
	public boolean update(String loginId, int id, Todo todo) {

		Todo record = new Todo();
		record.setDueDate(todo.getDueDate());
		record.setDescription(todo.getDescription());
		record.setDoneFlg(todo.getDoneFlg());
		record.setDoneAt(todo.getDoneAt());
		record.setLockVersion(todo.getLockVersion() + 1);

		TodoCriteria crit = new TodoCriteria();
		Criteria c = crit.createCriteria();
		c.andIdEqualTo(id);
		c.andPostedByEqualTo(loginId);
		c.andDeletedFlgEqualTo(DeletedFlag.NOT_DELETED);
		c.andLockVersionEqualTo(todo.getLockVersion());

		int count = todoMapper.updateByExampleSelective(record, crit);
		return count == 1;
	}
```


# STEP 13: TODO検索画面を作成する。(1)
最後に、TODO検索画面を作成します。

## 画面の仕様
画面の仕様を確認します。TODO検索は「検索画面」のみで構成されます。「検索画面」で検索条件を指定し、「検索」ボタンを押下すると、「検索画面」に検索結果を一覧表形式で表示します。

*	外部仕様
	*	検索画面
		*	URIパス
			*	初期表示: `/secure/todo/list`
			*	一覧表示: `/secure/todo/list/execute`
			*	ダウンロード: `/secure/todo/list/execute?download`
		*	入力フォーム
			*	登録日時
				*	範囲FROM: postedFrom
				*	範囲TO: postedTo
			*	期日
				*	範囲FROM: dueDateFrom
				*	範囲TO: dueDateTo
			*	完了フラグ
				*	完了: done
				*	未完了: notDone
			*	並び順
				*	ソート項目: orderBy
				*	ソート順: orderDir
			*	ページネーション
				*	ページ番号: pageNo
				*	1ページあたり件数: pageSz
		*	画面は「初期表示」と「一覧表示」とで共用する。検索条件の入力フォームはいずれでも表示する。
		*	検索条件を入力し「検索」ボタンを押下すると、「一覧表示」のURIパスへPOSTし検索処理を実行し、検索結果を一覧表形式で表示する。
		*	入力された検索条件をセッションに保持しておき、「検索」ボタンを押下するのでなしに、一覧表示のURIパスへアクセスすると、セッションに保持した検索条件に基づき検索処理を実行し、検索結果を一覧表形式で表示する。
		*	検索条件を入力して「ダウンロード」ボタンを押下すると、CSVファイルがダウンロードされる。
*	内部仕様
	*	共通
		*	フォーム: `cherry.spring.tutorial.web.secure.todo.list.TodoListForm`
		*	コントローラ
			*	インタフェース: `cherry.spring.tutorial.web.secure.todo.list.TodoListController`
			*	実装クラス: `cherry.spring.tutorial.web.secure.todo.list.TodoListControllerImpl`
		*	初期表示
			*	メソッド: `init`
				*	`ModelAndView`を作りビュー名を設定する。
				*	画面に表示するフォームを作成し、`ModelAndView`に設定する。
				*	`ModelAndView`を返却する。
			*	メソッド: `getForm`
				*	画面に表示するフォームを作成する。
				*	初期表示する値をフォームに設定する。
				*	フォームを返却する。
			*	JSP: /WEB-INF/tiles/secure/todo/list/init.jsp (ビュー名: secure/todo/list/init)
		*	一覧表示
			*	メソッド: `execute`
				*	引数として受渡されたフォームとバインド結果から入力値の妥当性を検証する。
				*	入力値をフォームから取得し、検索条件オブジェクト`SearchCondition`を作成する。
				*	検索処理を実行する。
				*	`ModelAndView`を作りビュー名を設定する。
				*	検索結果オブジェクト`SearchResult`をセットする。
				*	`ModelAndView`を返却する。
			*	JSP: /WEB-INF/tiles/secure/todo/list/init.jsp (ビュー名: secure/todo/list/init)
		*	ダウンロード
			*	メソッド: `download`
				*	引数として受渡されたフォームとバインド結果から入力値の妥当性を検証する。
				*	入力値をフォームから取得し、検索条件オブジェクト`SearchCondition`を作成する。
				*	ダウンロード処理を実行する。
			*	HTTPレスポンスとしてCSVデータが返却される。基本の画面遷移仕様としては「遷移先の画面はない」。(入力値不正の場合に画面にエラーメッセージを表示する必要があるので、まるっきり「画面遷移なし」という訳ではない)


## 技術要素
TODO検索画面では、下記3つの技術要素を中心に説明します。

*	検索とダウンロードの共用
*	動的なSQL形成 (Querydsl SQL)
*	日付、日時の範囲指定

### 検索とダウンロードの共用
検索画面において、検索結果を一覧形式で画面に表示する場合と、検索結果をCSV形式などのデータとしてダウンロードする場合とを考えます。

同じ条件で検索するのですから、使用するSQL文には類似性があります。ただし、求められる機能が異なるため、SQL自体にも微妙な差異があります。これを整理すると下表の通りです。即ち、概ね同じ内容で、用途に応じて、SELECT句、ORDER BY句、LIMIT OFFSET句を切替えます。

|                | 検索(件数) | 検索(抽出) | ダウンロード |
|:---------------|:----------:|:----------:|:------------:|
| SELECT句       | COUNT()    | 列を指定   | ←同じ       |
| FROM句         | 表を指定   | ←同じ     | ←同じ       |
| WHERE句        | 条件を指定 | ←同じ     | ←同じ       |
| GROUP BY句     | 条件を指定 | ←同じ     | ←同じ       |
| HAVING句       | 条件を指定 | ←同じ     | ←同じ       |
| ORDER BY句     | 指定しない | 条件を指定 | ←同じ       |
| LIMIT OFFSET句 | 指定しない | 条件を指定 | 指定しない   |

本チュートリアルでは、この切替えに対応したヘルパ (`SQLQueryHelper`) を使用して検索画面の機能を作成します。

### 動的なSQL形成 (Querydsl SQL)
検索画面では、画面に入力された検索条件に応じてSQL文を動的に形成する必要があります。
単純に「文字列を結合すればSQL文を作れる」といのは事実ではありますが、その選択肢はとるべきではありません。他にやりようがなく止むを得ない場合に仕方なく選択する、というレベルでしょう。文字列結合に走る前に、十分に調査＆検討しなければなりません。

本チュートリアルでは、Querydsl SQLを使用して動的にSQLを形成します。
Querydsl SQLの説明は[本家マニュアル](http://www.querydsl.com/static/querydsl/latest/reference/html/index.html "本家マニュアル")に譲るとして、ここでは検索条件の組立て方の実例より「こんな感じで書く」というのを紹介します。以下に使用実例と表現しようとしているSQLを示します。なお、Javaプログラムでは条件が指定されたか否かを判定して組立てていますが、SQLは全ての条件が指定された場合に形成されるものを示します。

```Java:TodoServiceImplより抜粋＆再構成
SQLQuery query = {大元の空クエリをヘルパが作成する};
SearchCondition cond = {検索条件を保持する};

QTodo t = new QTodo("t");

BooleanBuilder where = new BooleanBuilder();
where.and(t.postedBy.eq(loginId));
if (cond.getPostedFrom() != null) {
	where.and(t.postedAt.goe(cond.getPostedFrom()));
}
if (cond.getPostedTo() != null) {
	where.and(t.postedAt.lt(cond.getPostedTo()));
}
if (cond.getDueDateFrom() != null) {
	where.and(t.dueDate.goe(cond.getDueDateFrom()));
}
if (cond.getDueDateTo() != null) {
	where.and(t.dueDate.lt(cond.getDueDateTo()));
}
if (!cond.getDoneFlg().isEmpty()) {
	List<Integer> flg = new ArrayList<>();
	for (FlagCode f : cond.getDoneFlg()) {
		flg.add(f.code());
	}
	where.and(t.doneFlg.in(flg));
}
where.and(t.deletedFlg.eq(DeletedFlag.NOT_DELETED.code()));

query.from(t).where(where);
query.orderBy(t.postedAt.desc());
```

```SQL
FROM
	todo AS t
WHERE
	t.posted_at >= {投稿日時:範囲指定(FROM)}
	AND
	t.posted_at < {投稿日時:範囲指定(TO)}
	AND
	t.due_date >= {期日:範囲指定(FROM)}
	AND
	t.due_date < {期日:範囲指定(TO)}
	AND
	t.done_flg IN (0, 1)
	AND
	t.deleted_flg = 0
ORDER BY
	t.posted_at DESC
```

なお、プログラムの作成にあたっては、前節「検索とダウンロードの共用」で説明したように、共用できるところは共用し、用途で変えるところは変えるようにします。

### 日付、日時の範囲指定
検索条件として日付、日時の範囲を指定する場合は「範囲上限側の判定条件」の組立て方に注意する必要があります。
通常、データ的には秒よりも高い精度(ミリ秒やマイクロ秒)で値を管理しています。このため、範囲上限として入力された日時データ(秒単位と仮定)を「`列 <= 'YYYY/MM/DD HH:MM:SS'`」のように検索条件として指定すると、「HH:MM:SS.001からHH:MM:SS.999のデータが漏れる」という問題が顕在化します。

対策方法の選択肢はいくつかありますが、本チュートリアルでは「`列 < 'YYYY/MM/DD HH:MM:SS' + 1秒`」という指定の仕方をすることとします。


## 実装の仕方
TODO登録画面、TODO編集画面と同じように、何をしているか理解しながら進めることを意図し、少しずつ順を追って実装していきます。
実装は下記の4つの段階に分けて進めていきます。

*	基本的な画面遷移を実装する。
*	入力および妥当性検証NGの画面遷移を作成する。
*	TODO検索画面の主たる業務ロジックである「検索処理(一覧表示)」を実装する。
*	TODO検索画面の主たる業務ロジックである「検索処理(ダウンロード)」を実装する。

STEP 13では「基本的な画面遷移を実装」します。

## フォーム
まずはじめにフォームを定義します。検索条件は「登録日時(範囲)」「期日(範囲)」「完了フラグ」「ソート列」「ソート順」です。

日付、日時の範囲を条件として指定する場合は、範囲下限は`@CustomDateTimeFormat(Range.FROM)`、範囲上限は`@CustomDateTimeFormat(Range.TO)`を指定します。
アノテーション`@CustomDateTimeFormat`を付与することで、日付の場合は「YYYY/MM/DD」の形式で指定できるようになります。また、日時の場合は「YYYY/MM/DD(時分秒を省略)」「YYYY/MM/DD HH:MM(秒を省略)」「YYYY/MM/DD HH:MM:SS(省略なし)」の形式で指定できるようになります。また、`Range.TO`を指定すると、時分秒が省略された場合は「23:59:59」として、秒が省略された場合は「HH:MM:59」として解析します。なお、`Range.FROM`を指定すると、時分秒が省略された場合は「00:00:00」として、秒が省略された場合は「HH:MM:00」として解析します(事実上、`Range.FROM`を指定しない場合と同じです)。

フォームのプロパティには列挙型を指定することができます。ソート列(orderBy)、ソート順(orderDir)は列挙型で扱います。

ページネーションはページ番号(pageNo)と1ページあたりの件数(pageSz)で指定します。利用者が直接的に入力する項目ではありませんが、これらもフォームのプロパティに含めておきます。検索画面に「戻る」機能を実現する際に、それまでの表示(一覧表示)を復元する上で、ページネーションの内容も必要なためです。

以上を踏まえ、下記のようにフォームを作成します。

```Java:TodoListForm
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class TodoListForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@CustomDateTimeFormat(Range.FROM)
	private LocalDateTime postedFrom;

	@CustomDateTimeFormat(Range.TO)
	private LocalDateTime postedTo;

	@CustomDateTimeFormat(Range.FROM)
	private LocalDate dueDateFrom;

	@CustomDateTimeFormat(Range.TO)
	private LocalDate dueDateTo;

	private boolean done;

	private boolean notDone;

	private OrderBy orderBy;

	private OrderDir orderDir;

	private int pageNo;

	private int pageSz;

}
```

```Java:OrderBy
public enum OrderBy {
	NONE, ID, POSTED_AT, DUE_DATE
}
```

```Java:OrderDir
public enum OrderDir {
	ASC, DESC
}
```

## コントローラ
コントローラ定義のポイントは「ダウンロードのサポート」と「検索画面に戻った時の再検索」です。

ダウンロードのサポートのポイントは、同一の入力フォームを共有しつつ、押下するボタンに応じて、検索またはダウンロードの処理を切替える点にあります。これは、`@RequestMapping`アノテーションの`params`を指定することで、実行するメソッドを切替えることで実現します。
また、ダウンロードにあたっては、メソッド引数に`HttpServletResponse response`を追加します。

検索画面に戻った時の再検索は、入力された検索条件をフォームに保持し、これをセッションに格納して引回すことで実現します。具体的には、コントローラのインタフェースに`@SessionAttributes`アノテーションを付与します。`@SessionAttributes`アノテーションには、セッションで引回すフォームの名前(文字列)または型(クラス)を指定することができます。

### インタフェース
下記のようにコントローラのインタフェースを作成します。

```Java:TodoListController
@SessionAttributes(types = TodoListForm.class)
@RequestMapping(PathDef.URI_TODO_LIST)
public interface TodoListController {

	@ModelAttribute()
	TodoListForm getForm();

	@RequestMapping()
	ModelAndView init(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request);

	@RequestMapping(PathDef.SUBURI_EXECUTE)
	ModelAndView execute(@Validated TodoListForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request);

	@RequestMapping(value = PathDef.SUBURI_EXECUTE, params = PathDef.METHOD_DOWNLOAD)
	ModelAndView download(@Validated TodoListForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request, HttpServletResponse response);

}
```

### 実装クラス
STEP 13で、検索画面を初期表示するためのプログラム(`getForm()`メソッドと`init()`メソッド)を完成します。

`getForm()`メソッドでは、フォームに検索条件の初期値を入れて返却します。

`init()`メソッドでは、`getForm()`メソッドで生成したフォームを`ModelAndView`に格納します。`getForm()`メソッドには`@ModelAttribute`アノテーションが付与されていますので、`init()`メソッドでフォームを用意する必要はないように思われるかも知れません。明示的にフォームを`ModelAndView`に格納する意図は「初期表示の時は検索条件を初期化する」点にあります。`@SessionAttributes`を指定しているため、明示的にフォームを受渡さないと、セッションに残っているフォームが使われてしまう(初期値でなく、その前に入力した値が再表示される)からです。
なお、`@SessionAttributes`がなければ、明示的にフォームを受渡さなくても、検索条件は初期化されます。

下記のようにコントローラの実装クラスを作成します。

```Java:TodoListControllerImpl
@Controller
public class TodoListControllerImpl implements TodoListController {

	@Value("${tutorial.web.secure.todo.list.defaultOffsetOfDueDate}")
	private int defaultOffsetOfDueDate;

	@Value("${tutorial.web.secure.todo.list.defaultPageSize}")
	private int defaultPageSize;

	@Value("${tutorial.web.secure.todo.list.contentType}")
	private String contentType;

	@Value("${tutorial.web.secure.todo.list.filename}")
	private String filename;

	@Autowired
	private TodoService todoService;

	@Autowired
	private BizdateHelper bizdateHelper;

	@Autowired
	private SQLQueryHelper sqlQueryHelper;

	@Autowired
	private DownloadHelper downloadHelper;

	@Override
	public TodoListForm getForm() {
		TodoListForm form = new TodoListForm();
		form.setDueDateTo(bizdateHelper.today()
				.plusDays(defaultOffsetOfDueDate));
		form.setNotDone(true);
		form.setOrderBy(OrderBy.DUE_DATE);
		form.setOrderDir(OrderDir.DESC);
		return form;
	}

	@Override
	public ModelAndView init(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_LIST);
		mav.addObject(getForm());
		return mav;
	}

	@Override
	public ModelAndView execute(TodoListForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_LIST);
		return mav;
	}

	@Override
	public ModelAndView download(TodoListForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

}
```

```Init:tutorial.properties
tutorial.web.secure.todo.list.defaultOffsetOfDueDate=7
tutorial.web.secure.todo.list.defaultPageSize=10
tutorial.web.secure.todo.list.contentType=text/csv
tutorial.web.secure.todo.list.filename=todolist_{0}.csv
```

## JSP
STEP 13では画面は最小限とします。下記のように作成します。

```HTML:/WEB-INF/tiles/secure/todo/list/init.jsp
<h2>TODO検索</h2>
```

# STEP 14: TODO検索画面を作成する。(2)
STEP 14では「入力および妥当性検証NGの画面遷移を作成」します。

## コントローラ
妥当性検証NGの判定は下記の通りです。一覧表示、ダウンロードの両方で同じ判定をします。

*	入力値の単項目チェック(字面上の様式検証)
	*	NG => 検索画面に入力エラーメッセージを表示する。

正常に処理すると、一覧表示では画面に検索結果を表示し、ダウンロードではCSVデータを返却します。

以上を踏まえ、下記のようにコントローラを作成します。

### 実装クラス

```Java:TodoListControllerImpl
	@Override
	public ModelAndView execute(TodoListForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request) {

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_LIST);
			return mav;
		}

		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_LIST);
		return mav;
	}

	@Override
	public ModelAndView download(TodoListForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request, HttpServletResponse response) {

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_LIST);
			return mav;
		}

		return null;
	}
```

## JSP
JSPの作成項目は下記の通りです。

*	警告系メッセージ
	*	検索結果が0件である旨のメッセージ
*	エラー系メッセージ
	*	入力エラーメッセージ(単項目チェック)
*	入力フォーム
	*	表示する入力フォーム
		*	登録日時(範囲)
		*	期日(範囲)
		*	完了フラグ
		*	ソート列
		*	ソート順
		*	(隠し項目) ページ番号
		*	(隠し項目) 1ページあたり件数
	*	隠し入力フォーム (項目の内訳は表示する入力フォームと同じ)
*	ページネーションリンク
	*	カスタムタグで描画
	*	リンクで遷移させる処理は共通のJavaScript (general.js) に定義済み
		*	カスタムタグで描画した構造を走査し、clickイベントの処理として「前記「隠し入力フォーム」でsubmitする」処理を組込む。
*	検索結果の一覧表示

以上を踏まえ、下記のようにJSPを作成します。

```HTML:/WEB-INF/tiles/secure/todo/list/init.jsp
<h2>TODO検索</h2>
<s:hasBindErrors name="todoListForm">
	<div class="form-group has-error">
		<div class="help-block bg-danger">
			<f:errors path="todoListForm" element="div" />
			<s:nestedPath path="todoListForm">
				<f:errors path="postedFrom" element="div" />
				<f:errors path="postedTo" element="div" />
				<f:errors path="dueDateFrom" element="div" />
				<f:errors path="dueDateTo" element="div" />
				<f:errors path="done" element="div" />
				<f:errors path="notDone" element="div" />
				<f:errors path="orderBy" element="div" />
				<f:errors path="orderDir" element="div" />
			</s:nestedPath>
		</div>
	</div>
</s:hasBindErrors>
<c:if
	test="${searchResult != null && searchResult.resultList.isEmpty()}">
	<div class="has-warning">
		<div class="help-block">条件に該当する項目はありませn。</div>
	</div>
</c:if>
<f:form servletRelativeAction="/secure/todo/list/execute" method="POST"
	modelAttribute="todoListForm" role="form">
	<div class="form-group">
		<f:label path="postedFrom" cssErrorClass="has-error">登録日時</f:label>
		<f:input path="postedFrom" cssErrorClass="has-error" />
		-
		<f:input path="postedTo" cssErrorClass="has-error" />
	</div>
	<div class="form-group">
		<f:label path="dueDateFrom" cssErrorClass="has-error">期日</f:label>
		<f:input path="dueDateFrom" cssErrorClass="has-error" />
		-
		<f:input path="dueDateTo" cssErrorClass="has-error" />
	</div>
	<div class="form-group">
		<f:checkbox path="notDone" label="未完了" cssErrorClass="has-error" />
		<f:checkbox path="done" label="完了" cssErrorClass="has-error" />
	</div>
	<div class="form-group">
		<f:label path="orderBy" cssErrorClass="has-error">ソート列</f:label>
		<f:select path="orderBy" cssErrorClass="has-error">
			<c:set var="orderByList"
				value="${common:getLabeledEnumList('cherry.spring.tutorial.web.secure.todo.OrderBy')}" />
			<f:options itemValue="enumName" itemLabel="enumLabel"
				items="${orderByList}" />
		</f:select>
	</div>
	<div class="form-group">
		<f:label path="orderDir" cssErrorClass="has-error">ソート順</f:label>
		<c:set var="orderDirList"
			value="${common:getLabeledEnumList('cherry.spring.tutorial.web.secure.todo.OrderDir')}" />
		<f:radiobuttons path="orderDir" itemValue="enumName"
			itemLabel="enumLabel" items="${orderDirList}"
			cssErrorClass="has-error" />
	</div>
	<input type="hidden" name="pageNo" value="0" />
	<f:hidden path="pageSz" />
	<f:button type="submit" class="btn btn-default">検索</f:button>
	<f:button type="submit" class="btn btn-default" name="download">ダウンロード</f:button>
</f:form>
<c:if
	test="${searchResult != null && !searchResult.resultList.isEmpty()}">
	<f:form servletRelativeAction="/secure/todo/list/execute" method="POST"
		modelAttribute="todoListForm" id="todoListFormHidden"
		class="app-pager-form">
		<f:hidden path="postedFrom" id="postedFromHidden" />
		<f:hidden path="postedTo" id="postedToHidden" />
		<f:hidden path="dueDateFrom" id="dueDateFromHidden" />
		<f:hidden path="dueDateTo" id="dueDateToHidden" />
		<f:hidden path="notDone" id="notDoneHidden" />
		<f:hidden path="done" id="doneHidden" />
		<f:hidden path="orderBy" id="orderByHidden" />
		<f:hidden path="orderDir" id="orderDirHidden" />
		<f:hidden path="pageNo" id="pageNoHidden" cssClass="app-page-no" />
		<f:hidden path="pageSz" id="pageSzHidden" cssClass="app-page-sz" />
	</f:form>
	<mytag:pagerLink pageSet="${searchResult.pageSet}" />
	<table class="table table-striped">
		<thead>
			<tr>
				<th>#</th>
				<th>ID</th>
				<th>投稿日時</th>
				<th>期日</th>
				<th>状態</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="count" begin="1"
				end="${searchResult.resultList.size()}">
				<s:nestedPath path="searchResult.resultList[${count - 1}]">
					<s:bind path="id">
						<c:url var="url" value="/secure/todo/${status.value}" />
					</s:bind>
					<tr>
						<td>${searchResult.pageSet.current.from + count}</td>
						<td><a href="${url}"><s:bind path="id">${status.value}</s:bind></a></td>
						<td><a href="${url}"><s:bind path="postedAt">${status.value}</s:bind></a></td>
						<td><s:bind path="dueDate">${status.value}</s:bind></td>
						<td><s:bind path="doneFlg">${status.actualValue.isTrue() ? "完了" : "未完了" }</s:bind></td>
					</tr>
				</s:nestedPath>
			</c:forEach>
		</tbody>
	</table>
	<mytag:pagerLink pageSet="${searchResult.pageSet}" />
</c:if>
```

## message/code.propertiesファイル (区分値の表示名)

```Ini:message/code.properties
cherry.spring.tutorial.web.secure.todo.OrderBy.NONE=なし
cherry.spring.tutorial.web.secure.todo.OrderBy.ID=ID
cherry.spring.tutorial.web.secure.todo.OrderBy.POSTED_AT=登録日時
cherry.spring.tutorial.web.secure.todo.OrderBy.DUE_DATE=期日
cherry.spring.tutorial.web.secure.todo.OrderDir.ASC=昇順
cherry.spring.tutorial.web.secure.todo.OrderDir.DESC=降順
```


# STEP 15: TODO検索画面を作成する。(3)
STEP 15では「TODO検索画面の主たる業務ロジックである「検索処理(一覧表示)」を実装」します。

## コントローラ
コントローラは下記の要領で処理を進めます。

*	フォームに受渡されたデータ(入力値＆隠し項目)を検索条件インスタンス`SearchCondition`に詰替える。
	*	日付範囲、日時範囲は、本チュートリアルが提供するユーティリティで算出する(STEP 13で述べた技術要素はユーティリティにて取込まれる)。
*	サービス`TodoService`の検索メソッド(後述)を呼出す。
*	検索結果を`ModelAndView`に格納して返却する。

以上を踏まえ、下記のようにコントローラを作成します。

### 実装クラス

```Java:TodoListControllerImpl
	@Override
	public ModelAndView execute(TodoListForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request) {

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_LIST);
			return mav;
		}

		SearchCondition cond = createCondition(form);
		int pageNo = form.getPageNo();
		int pageSz = form.getPageSz() <= 0 ? defaultPageSize : form.getPageSz();

		SearchResult result = todoService.searh(auth.getName(), cond, pageNo,
				pageSz);

		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_LIST);
		mav.addObject(result);
		return mav;
	}

	private SearchCondition createCondition(TodoListForm form) {

		SearchCondition cond = new SearchCondition();
		cond.setPostedFrom(LocalDateTimeUtil.rangeFrom(form.getPostedFrom()));
		cond.setPostedTo(LocalDateTimeUtil.rangeTo(form.getPostedTo()));
		cond.setDueDateFrom(LocalDateUtil.rangeFrom(form.getDueDateFrom()));
		cond.setDueDateTo(LocalDateUtil.rangeTo(form.getDueDateTo()));

		List<FlagCode> doneFlg = new ArrayList<>();
		if (form.isDone()) {
			doneFlg.add(FlagCode.TRUE);
		}
		if (form.isNotDone()) {
			doneFlg.add(FlagCode.FALSE);
		}
		cond.setDoneFlg(doneFlg);

		cond.setOrderBy(form.getOrderBy());
		cond.setOrderDir(form.getOrderDir());

		return cond;
	}
```

## サービス
サービス`TodoService`に検索処理を`search`メソッドとして作成します。
STEP 13で述べたように、Querydsl SQLを使用して動的SQLを形成します。SQLの形成にあたっては、STEP 13で述べたように、全SQLで共通、ORDER BY句、LIMIT OFFSET句、SELECT句とを、別々に構成します(そのようにヘルパ`SQLQueryHelper`のAPIは設計されています)。

以上を踏まえ、下記のようにサービスを作成します。

### インタフェース

```Java:TodoService
	SearchResult searh(String loginId, SearchCondition cond, int pageNo,
			int pageSz);
```

### 実装クラス
```Java:TodoServiceImpl
	@Autowired
	private SQLQueryHelper sqlQueryHelper;

	@Autowired
	private RowMapperCreator rowMapperCreator;

	@Transactional(readOnly = true)
	@Override
	public SearchResult searh(String loginId, SearchCondition cond, int pageNo,
			int pageSz) {
		QTodo t = new QTodo("t");
		SQLQueryResult<Todo> r = sqlQueryHelper.search(
				configurer(t, loginId, cond), pageNo, pageSz,
				rowMapperCreator.create(Todo.class), columns(t));
		SearchResult result = new SearchResult();
		result.setPageSet(r.getPageSet());
		result.setResultList(r.getResultList());
		return result;
	}

	private Expression<?>[] columns(QTodo t) {
		return new Expression<?>[] { t.id, t.postedBy, t.postedAt, t.dueDate,
				t.doneAt, t.doneFlg, t.description, t.updatedAt, t.createdAt,
				t.lockVersion, t.deletedFlg };
	}

	private SQLQueryConfigurer configurer(final QTodo t, final String loginId,
			final SearchCondition cond) {
		return new SQLQueryConfigurer() {

			@Override
			public SQLQuery configure(SQLQuery query) {

				BooleanBuilder where = new BooleanBuilder();
				where.and(t.postedBy.eq(loginId));
				if (cond.getPostedFrom() != null) {
					where.and(t.postedAt.goe(cond.getPostedFrom()));
				}
				if (cond.getPostedTo() != null) {
					where.and(t.postedAt.lt(cond.getPostedTo()));
				}
				if (cond.getDueDateFrom() != null) {
					where.and(t.dueDate.goe(cond.getDueDateFrom()));
				}
				if (cond.getDueDateTo() != null) {
					where.and(t.dueDate.lt(cond.getDueDateTo()));
				}
				if (!cond.getDoneFlg().isEmpty()) {
					List<Integer> flg = new ArrayList<>();
					for (FlagCode f : cond.getDoneFlg()) {
						flg.add(f.code());
					}
					where.and(t.doneFlg.in(flg));
				}
				where.and(t.deletedFlg.eq(DeletedFlag.NOT_DELETED.code()));

				return query.from(t).where(where);
			}

			@Override
			public SQLQuery orderBy(SQLQuery query) {
				if (cond.getOrderBy() == OrderBy.NONE) {
					return query;
				} else if (cond.getOrderBy() == OrderBy.ID) {
					if (cond.getOrderDir() == OrderDir.DESC) {
						return query.orderBy(t.id.desc());
					} else {
						return query.orderBy(t.id.asc());
					}
				} else if (cond.getOrderBy() == OrderBy.POSTED_AT) {
					if (cond.getOrderDir() == OrderDir.DESC) {
						return query.orderBy(t.postedAt.desc());
					} else {
						return query.orderBy(t.postedAt.asc());
					}
				} else if (cond.getOrderBy() == OrderBy.DUE_DATE) {
					if (cond.getOrderDir() == OrderDir.DESC) {
						return query.orderBy(t.dueDate.desc());
					} else {
						return query.orderBy(t.dueDate.asc());
					}
				} else {
					return query;
				}
			}
		};
	}
```

### 検索条件

```Java:SearchCondition
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class SearchCondition implements Serializable {

	private static final long serialVersionUID = 1L;

	private LocalDateTime postedFrom;

	private LocalDateTime postedTo;

	private LocalDate dueDateFrom;

	private LocalDate dueDateTo;

	private List<FlagCode> doneFlg;

	private OrderBy orderBy;

	private OrderDir orderDir;

}
```

### 検索結果

```Java:SearchResult
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class SearchResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private PageSet pageSet;

	private List<Todo> resultList;

}
```


# STEP 16: TODO検索画面を作成する。(4)
STEP 16では「TODO検索画面の主たる業務ロジックである「検索処理(ダウンロード)」を実装」します。

## コントローラ
コントローラは下記の要領で処理を進めます。

*	フォームに受渡されたデータ(入力値＆隠し項目)を検索条件インスタンス`SearchCondition`に詰替える。
	*	日付範囲、日時範囲は、本チュートリアルが提供するユーティリティで算出する(STEP 13で述べた技術要素はユーティリティにて取込まれる)。
*	ダウンロードデータを生成するためのアクション`DownloadAction`を用意する。
	*	同アクションの中で、サービス`TodoService`のエクスポートメソッド(後述)を呼出すようにする。
*	上記アクションを指定して、ダウンロードヘルパ(`DownloadHelper`)を呼出す。
	*	ヘルパの中でダウンロード用の共通処理(レスポンスヘッダのセットアップ、出力先の準備)を実施し、
	*	上記アクションが呼出される。
	*	ストリームのクローズはヘルパの中で実施される。
*	ダウンロード処理を実行した後は、画面(HTML)を描画してはならないので、メソッドは`null`を返却する。

以上を踏まえ、下記のようにコントローラを作成します。

### 実装クラス

```Java:TodoListControllerImpl
	@Override
	public ModelAndView download(TodoListForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request, HttpServletResponse response) {

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_LIST);
			return mav;
		}

		final String loginId = auth.getName();
		final SearchCondition cond = createCondition(form);

		DownloadAction action = new DownloadAction() {
			@Override
			public int doDownload(Writer writer) throws IOException {
				return todoService.export(writer, loginId, cond);
			}
		};
		downloadHelper.download(response, contentType, filename,
				bizdateHelper.now(), action);

		return null;
	}
```

## サービス
サービス`TodoService`にエクスポート処理を`export`メソッドとして作成します。
動的SQLの形成は、STEP 14で用意したものをそのまま使います(そのまま使えるようにヘルパ`SQLQueryHelper`のAPIは設計されています)。

以上を踏まえ、下記のようにサービスを作成します。

### インタフェース

```Java:TodoService
	int export(Writer writer, String loginId, SearchCondition cond);
```

### 実装クラス

```Java:TodoServiceImpl
	@Transactional(readOnly = true)
	@Override
	public int export(Writer writer, String loginId, SearchCondition cond) {
		try {
			QTodo t = new QTodo("t");
			return sqlQueryHelper.download(configurer(t, loginId, cond),
					new CsvConsumer(writer, true), new NoneLimiter(),
					columns(t));
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}
```

以上。
