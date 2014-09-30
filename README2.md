Spring MVCチュートリアル(2)
=========================

# 概要
*	STEP 10: TODO編集画面を作成する。(1)
*	STEP 11: TODO編集画面を作成する。(2)
*	STEP 12: TODO編集画面を作成する。(3)


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
			*	インタフェース: `cherry.spring.tutorial.web.secure.todo.create.TodoEditController`
			*	実装クラス: `cherry.spring.tutorial.web.secure.todo.create.TodoEditControllerImpl`
	*	編集画面
		*	メソッド: `init`
			*	パス変数に指定された主キー値を持つTODOレコードを照会する。
			*	`ModelAndView`を作りビュー名を設定する。
			*	画面に表示するフォームを作成し、`ModelAndView`に設定する。
			*	`ModelAndView`を返却する。
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
*	入力および妥当性検証の画面遷移を作成する。
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
STEP 10で、編集画面を表示するためのプログラム(`init`メソッド)を完成します。

TODO登録画面では、初期表示するフォーム(`TodoCreateForm`)を返却するメソッド`getForm()`を別に設け、`init`メソッドではビューの名前を返却するだけでした。
TODO編集画面では、初期表示するフォーム(`TodoEditForm`)の値は「パス変数で指定された主キーを持つTODOレコード」です。パス変数の値に依存してフォームを作成する必要がありますので、フォームの作成も`init`メソッド中で行います。TODOレコードの照会は、TODO登録画面の「完了画面」を表示するために作成したものを使います。取得したレコードの値を`TodoEditForm`に詰め直して(`createForm()`メソッド)、これを`ModelAndView`に格納(`addObject()`メソッド)します。

また、メソッド引数として受取ったパス変数の値も`ModelAndView`に格納してJSPに受渡します。URIパスから抽出されたパス変数の値は、そのままではJSPには渡されないため、コントローラのメソッドの中で明示的に受渡します。

あと、TODOレコードを照会した結果は`Contract.shouldExist()`メソッドで存在保証します。
Todoオブジェクトが存在しない(== null)場合は`NotFoundException`がthrowされ、後続の処理がスキップされます。また`NotFoundException`は別途定義済みの`@ExceptionHandler`が拾い、HTTPステータス404 (Not Found)として返却されます。

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
	public ModelAndView init(int id, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {

		Todo todo = todoService.findById(auth.getName(), id);
		Contract.shouldExist(todo, Todo.class, auth.getName(), id);

		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
		mav.addObject(PathDef.PATH_VAR_ID, id);
		mav.addObject(createForm(todo));
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

	private TodoEditForm createForm(Todo todo) {
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

}
```

## JSP
STEP 10では画面は最小限とします。下記のように作成します。

```HTML:/WEB-INF/tiles/secure/todo/edit/init.jsp
<h2>TODO編集</h2>
```


# STEP 11: TODO編集画面を作成する。(2)

STEP 11では「画面の入力および妥当性検証の画面遷移を作成」します。

## コントローラ

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

# STEP 12: TODO編集画面を作成する。(3)

STEP 12では「TODO編集画面の主たる業務ロジックである「DBのTODOレコードを更新する」を実装」します。

## コントローラ

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

### インタフェース

```Java:TodoService
public interface TodoService {

	Integer create(Todo todo);

	boolean update(String loginId, int id, Todo todo);

	Todo findById(String loginId, int id);

}
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

以上。
