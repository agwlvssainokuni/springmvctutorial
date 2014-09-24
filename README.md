Spring MVCチュートリアル
========================

# 概要
*	STEP 00: DBアクセスコードを生成する。
*	STEP 01: ホーム画面を作成する。
*	STEP 02: ログイン画面を作成する。
*	STEP 03: TODO登録画面を作成する、その1。


# STEP 00: DBアクセスコードを生成する。

DBアクセスにあたっては [MyBatis](http://mybatis.github.io/mybatis-3/ "MyBatis") をメインとして使用します。動的に条件を組立てるSQLを発行するケースでは [Querydsl](http://www.querydsl.com "Querydsl") (特に [Querydsl SQL](https://github.com/querydsl/querydsl/tree/master/querydsl-sql "Querydsl SQL")) を使用します。
なお、DBアクセス用にコード生成ツールの [MyBatis Generator](http://mybatis.github.io/generator/ "MyBatis Generator") と [Querydsl codegen](https://github.com/querydsl/codegen "Querydsl codegen") を使用します。

```bash:コマンドライン
$ mvn clean compile flyway:migrate
$ mvn mybatis-generator:generate
$ mvn querydsl:export
```


# STEP 01: ホーム画面を作成する。

単純な画面表示から始めましょう。
画面を表示するには「コントローラ」と「JSP」を作成します。これを順に作っていきます。

## 画面の仕様
まず画面の仕様を確認します。ホーム画面は下記の通りです。

*	外部仕様
	*	URIパス: `/secure/`
	*	TODO登録画面、TODO検索画面へのリンクを配置する。
*	内部仕様
	*	コントローラ
		*	インタフェース: `cherry.spring.tutorial.web.secure.home.HomeController`
		*	実装クラス: `cherry.spring.tutorial.web.secure.home.HomeControllerImpl`
	*	JSP: `/WEB-INF/tiles/secure/home/init.jsp` (ビュー名: `secure/home/init`)

## コントローラ
### インタフェース
Spring MVC ではコントローラのインタフェースに「当該インタフェースが処理するURIパス」を、アノテーション`@RequestMapping(URIパス)`で定義します。アノテーションは、インタフェースとメソッドの両方に指定することができます。メソッドに指定したパスは、インタフェースに指定したURIパスからの相対パス (サブパス) として扱われます。
アノテーションに指定するURIパスは定数文字列である必要があります。実プロジェクトでは、URIパスは予め用意された定数定義を参照することが基本です。本チュートリアルでは、`PathDef`クラスに定数定義してありますので、これを参照します。

最初に作成する「ホーム画面」はURIパス一つなので、インタフェースに定義するメソッドも一つです。
Spring MVC は、アノテーションで指定することが前提であり、使い方のバリエーションは多岐に亘ります。ただし、それでは自由度が高すぎるため、実際のプロジェクトではクラス＆メソッドの定型パターンを定義し、それ従う形をとります。
本チュートリアルでは、下記をクラス＆メソッドの定型パターンとします。

*	クラス名: `XxxController`(インタフェース)、`XxxControllerImpl`(実装クラス)
*	メソッド名: `init`(初期画面)、`confirm`(確認画面)、`execute`(主処理)、`finish`(完了画面)、`download`(ダウンロード)
*	引数
	*	入力値を受取る画面で指定する
		*	`XxxForm form`
		*	`BindingResult binding`
	*	ページネーションする画面で指定する (ページネーションの情報をセッションに入れる場合は、ここに指定せずに、`form`に入れる)
		*	`int pageNo`
		*	`int pageSz`
	*	認証要画面で必須
		*	`Authentication auth`
	*	全画面で必須
		*	`Locale locale`
		*	`SitePreference sitePref`
		*	`HttpServletRequest request`
	*	ダウンロード機能で指定する
		*	`HttpServletResponse response`
	*	HTTPリダイレクトの元と先で指定する
		*	`RedirectAttributes redirAttr`
*	返却値: `ModelAndView`

これに基づきインタフェースを下記の通り定義します。なお、`package`や`import`は省略しています(以下同様)。

```Java:HomeController
@RequestMapping(PathDef.URI_HOME)
public interface HomeController {

	@RequestMapping()
	ModelAndView init(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request);

}
```

### 実装クラス
コントローラインタフェースを実装 (`implements`) します。また、実装クラスにアノテーション`@Controller`を指定することで、Spring MVC が自動的にインスタンス登録します (Java以外に設定ファイルを記述する必要はありません)。
実装クラスでは、業務ロジックを実行し、表示する画面の名前「ビュー名」を`ModelAndView`に入れて返却します。画面に描画するデータがあればそれも`ModelAndView`に入れますが、ホーム画面には描画するデータがありませんので、何も入れません。

これを踏まえ実装クラスを下記の通り定義します。

```Java:HomeControllerImpl
@Controller
public class HomeControllerImpl implements HomeController {

	@Override
	public ModelAndView init(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_HOME);
		return mav;
	}

}
```

## JSP
Spring MVC でも、ビューにJSPを使用する場合は、いわゆるJSPの記法でOKです。JSP, EL, [JSTL 1.2](https://jstl.java.net "JSTL 1.2"), [Spring Tag](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/spring.tld.html "Spring Tag"), [Spring Form Tag](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/spring-form.tld.html "Spring Form Tag"), [Spring Security Tag](http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#taglibs "Spring Security Tag") が使えます。
また、本チュートリアルでは[Apache Tiles3](https://tiles.apache.org "Apache Tiles3")を使っています。実際に記述するJSPはbody要素の中身(のコンテンツ領域)のみです。

ホーム画面のJSPを下記の通り定義します。(下記の`c:`は JSTL の Core Tag Library です)

```HTML:/WEB-INF/tiles/secure/home/init.jsp
<h2>ホーム</h2>
<ul>
	<li><a href="<c:url value="/secure/todo/create" />">TODO登録</a></li>
	<li><a href="<c:url value="/secure/todo/list" />">TODO検索</a></li>
</ul>
```


# STEP 02: ログイン画面を作成する。

複雑な画面遷移制御をともなう画面の例としてログイン画面を作成します。

## 画面の仕様
画面の仕様を確認します。ログイン画面は下記の通りです。

*	外部仕様
	*	URIパス: `/login`
	*	入力フォーム
		*	POST先のURIパス: `/login/req`
		*	ログインIDのパラメタ名: `loginId`
		*	パスワードのパラメタ名: `password`
	*	ログイン処理の実態は [Spring Security](http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/ "Spring Security")。
		*	ログイン成功: `/secure/`へリダイレクト。
		*	ログイン失敗: `/login?loginFailed`へリダイレクト。
		*	ログアウト: `/login?loggedOut`へリダイレクト
*	内部仕様
	*	コントローラ
		*	インタフェース: `cherry.spring.tutorial.web.login.LoginController`
		*	実装クラス: `cherry.spring.tutorial.web.login.LoginControllerImpl`
	*	JSP: `/WEB-INF/tiles/login/init.jsp` (ビュー名: `login/init`)
	*	ログイン成功/失敗、ログアウトのメッセージ表示
		*	フラッシュスコープを使用してメッセージを表示する。
		*	`/login?loginFailed`, `/login?loggedOut`のロジックの中で、フラッシュスコープにデータを入れて、`/login`へリダイレクトする。
		*	`/login`のロジックの中でフラッシュスコープのデータをJSPに受け渡すようにする。`/login`のJSPの中で受け渡されたデータに応じてメッセージの表示を切り替える。
		*	`/login`を再表示するとメッセージは再描画されない(フラッシュスコープの特性 = 一回こっきり)。

## コントローラ
### 概観
ログイン画面では、Spring MVC の下記の3つの技術要素を使用します。

*	同一のURIパスに対して、リクエストパラメタによって、呼出されるメソッドを切替える。
*	リダイレクトする。
*	リダイレクト元からリダイレクト先へ、フラッシュスコープ(一回こっきり)でデータを受渡す。

#### 呼出されるメソッドを切替える
アノテーション`@RequestMapping`の`params`を指定することで、当該メソッドが呼出される条件にURIパス名にリクエストパラメタを追加することができます。
典型的には、`@RequestMapping(params = "パラメタ名=パラメタ値")`、`@RequestMapping(params = "パラメタ名")`という指定をします (既出のURIパス指定も`values = "URIパス"`の形で併用できます)。

昔ながらのフレームワークだと「一つの入力フォームにボタンを複数配置して、どれが押下されたかによってPOST先を切り替える」という制御を実装するには、「クライアントサイドでJavaScriptを使いform要素のonclickでaction属性を書替える」というやり方が使われていました。この制御が不要になります。
なお、「パラメタ名=パラメタ値」を指定することを踏まえ、入力画面に描画するボタンは、input要素(type="submit")ではなく、button要素(name属性とvalue属性を指定)を使用するようにしてください。

#### リダイレクトする
リダイレクトする場合も、コントローラのメソッドの返却値は`ModelAndView`です。このインスタンスに`setView(new RedirectView(リダイレクト先URI, true)`の形でリダイレクト先をセットして返却することでリダイレクトされます。また、`RedirectView`コンストラクタの第二引数はリダイレクト先をコンテキストパスからの相対パスで指定するか否かを表し、通常は true を指定します。

リダイレクト先URIは文字列で指定します。URIパスの特定にあたっては、Spring MVC のAPIの`MvcUriComponentsBuilder`の`fromMethodName()`メソッドを使用してください。第1引数にコントローラのインタフェースのクラスオブジェクト(`XxxController.class`)、第2引数にメソッド名(定数定義を参照、本チュートリアルでは`PathDef.METHOD_XXX`)、第3引数以降は可変引数で第2引数に指定したメソッドのシグネチャに合わせて指定してください。
第3引数以降は、リフレクションでメソッドを特定するための情報です。メソッドを特定できるならばnullを指定しても動作しますが、出来るだけ意味のある引数を渡してください。また、リダイレクト先にパス変数が含まれる場合は、第3引数以降に指定した値を該当箇所に埋込んだ形でURIを形成してくれます。

#### フラッシュスコープでデータを受渡す
リダイレクト元のメソッドの引数に`RedirectAttributes redirAttr`を指定してください。
リダイレクト元のメソッドの中で、`redirAttr.addFlashAttribute()`を呼出すことで、フラッシュスコープにデータを入れることができます。
リダイレクト先のコントローラのメソッドですることは特にありません。メソッドが`ModelAndView`を返却すると、Spring MVCがフラッシュスコープに入っているデータを同`ModelAndView`に入れ直した上でビュー(JSP)に渡されます。

なお、リダイレクト先のメソッドの中でフラッシュスコープで引渡されたデータを参照する場合は、リダイレクト先のメソッドの引数にも`RedirectAttributes redirAttr`を指定してください。`redirAttr.getFlashAttributes()`でフラッシュスコープに入っているデータを参照することが出来ます。

### インタフェース
インタフェースは下記の通りです。

```Java:LoginController
@RequestMapping(PathDef.URI_LOGIN)
public interface LoginController {

	@RequestMapping()
	ModelAndView init(Locale locale, SitePreference sitePref,
			HttpServletRequest request);

	@RequestMapping(params = "loginFailed")
	ModelAndView loginFailed(Locale locale, SitePreference sitePref,
			HttpServletRequest request, RedirectAttributes redirAttr);

	@RequestMapping(params = "loggedOut")
	ModelAndView loggedOut(Locale locale, SitePreference sitePref,
			HttpServletRequest request, RedirectAttributes redirAttr);

}
```

### 実装クラス
実装クラスは下記の通りです。

```Java:LoginControllerImpl
@Controller
public class LoginControllerImpl implements LoginController {

	@Override
	public ModelAndView init(Locale locale, SitePreference sitePref,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_LOGIN);
		return mav;
	}

	@Override
	public ModelAndView loginFailed(Locale locale, SitePreference sitePref,
			HttpServletRequest request, RedirectAttributes redirAttr) {

		redirAttr.addFlashAttribute("loginFailed", true);

		UriComponents redirTo = MvcUriComponentsBuilder.fromMethodName(
				LoginController.class, PathDef.METHOD_INIT, locale, sitePref,
				request).build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(redirTo.toUriString(), true));
		return mav;
	}

	@Override
	public ModelAndView loggedOut(Locale locale, SitePreference sitePref,
			HttpServletRequest request, RedirectAttributes redirAttr) {

		redirAttr.addFlashAttribute("loggedOut", true);

		UriComponents redirTo = MvcUriComponentsBuilder.fromMethodName(
				LoginController.class, PathDef.METHOD_INIT, locale, sitePref,
				request).build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(redirTo.toUriString(), true));
		return mav;
	}

}
```

## JSP
ログイン画面のJSPを下記の通り定義します。

```HTML:/WEB-INF/tiles/login/init.jsp
<h2>ログイン</h2>
<c:if test="${loginFailed}">
	<div class="has-error">
		<div class="help-block">ログインに失敗しました。</div>
	</div>
</c:if>
<c:if test="${loggedOut}">
	<div class="has-success">
		<div class="help-block">ログアウトしました。</div>
	</div>
</c:if>
<form id="login" action="<c:url value="/login/req" />" method="POST"
	role="form">
	<div class="form-group">
		<label for="loginId">ログインID</label>
		<input type="text" id="loginId" name="loginId" class="form-control" />
	</div>
	<div class="form-group">
		<label for="password">パスワード</label>
		<input type="password" id="password" name="password" class="form-control" />
	</div>
	<button class="btn btn-default" type="submit">ログイン</button>
</form>
```

# STEP 03: TODO登録画面を作成する、その1。

いよいよ入力フォームを持つ画面を作成します。作成すものは、これまでと同様の「コントローラ」と「JSP」に加え、入力値を保持する「フォーム」です。

## 画面の仕様

画面の仕様を確認します。TODO登録は、「入力画面」「確認画面」「登録処理」「完了画面」の順に遷移する形で動作します。

*	外部仕様
	*	入力画面
		*	URIパス: `/secure/todo/create`
		*	入力フォーム
			*	TODOの期日のパラメタ名: `dueDate`
			*	TODOの内容のパラメタ名: `description`
		*	「確認」ボタンを押下すると、「確認画面」へ遷移する。
	*	確認画面
		*	URIパス: `/secure/todo/create/confirm`
		*	表示項目: 「入力画面」で入力された値
		*	「登録」ボタンを押下すると、「登録処理」へPOSTする。
	*	登録処理
		*	URIパス: `/secure/todo/create/execute`
		*	POSTされた値をDBに登録し、「完了画面」へHTTPリダイレクトする(画面遷移する)。
	*	完了画面
		*	URIパス: `/secure/todo/create/confirm`
		*	表示項目: 「登録処理」で登録したTODOの内容
			*	TODOの期日 (入力値)
			*	TODOの内容 (入力値)
			*	TODO番号 (登録処理で発行された主キー)
			*	登録日時 (登録処理を実施した業務日時)
*	内部仕様
	*	共通
		*	フォーム: `cherry.spring.tutorial.web.secure.todo.create.TodoCreateForm`
		*	コントローラ
			*	インタフェース: `cherry.spring.tutorial.web.secure.todo.create.TodoCreateController`
			*	実装クラス: `cherry.spring.tutorial.web.secure.todo.create.TodoCreateControllerImpl`
	*	入力画面
		*	メソッド: `init`
			*	`ModelAndView`を作りビュー名を設定する。
			*	`ModelAndView`を返却する。
		*	JSP: `/WEB-INF/tiles/secure/todo/create/init.jsp` (ビュー名: `secure/todo/create/init`)
	*	確認画面
		*	メソッド: `confirm`
			*	引数として受渡されたフォームとバインド結果から入力値の妥当性を検証する。
			*	`ModelAndView`を作りビュー名を設定する。
			*	`ModelAndView`を返却する。
		*	JSP: `/WEB-INF/tiles/secure/todo/create/confirm.jsp` (ビュー名: `secure/todo/create/confirm`)
	*	登録処理
		*	メソッド: `execute`
			*	引数として受渡されたフォームとバインド結果から入力値の妥当性を検証する。
			*	入力値をフォームから取得し、DBにTODOレコードを作成する。
			*	作成したレコードの主キー(id)をフラッシュスコープに保管する。
			*	`ModelAndView`を作りリダイレクト先のURIパスを設定する。
			*	形成した`ModelAndView`を返却する。
		*	完了画面へHTTPリダイレクトする。
	*	完了画面
		*	メソッド: `finish`
			*	フラッシュスコープに保管された主キー(id)を取出し、該当するTODOレコードをDB照会する。
			*	`ModelAndView`を作りビュー名を設定する。また、`ModelAndView`にTODOレコードを設定する。
			*	形成した`ModelAndView`を返却する。
		*	JSP: `/WEB-INF/tiles/secure/todo/create/finish.jsp` (ビュー名: `secure/todo/create/finish`)

## コントローラのメソッドの基本構成
コントローラのメソッドは原則として下記の構成をとるようにしてください。なお、処理の内容に用事て実施しないこともあり得ます。上記に記述したメソッドの内部仕様もこれに則っています。

*	入力値の妥当性を検証する。
	*	単項目チェック (入力値の字面上の様式をチェックする)。
	*	項目間チェック (複数項目に亘る依存性に基づいて入力値をチェックする)。
	*	データ整合性チェック (マスタテーブル、区分値、設定値との整合性をチェックする)。
*	主たる業務ロジックを実行する。
	*	DBの照会や更新処理 (CRUD)。
	*	メッセージキューの操作 (非同期処理を実行登録)。
	*	フラッシュスコープにデータをセットする。
	*	フラッシュスコープからデータを取出す。
*	返却値を形成する。
	*	`ModelAndView`を形成する。
	*	ビュー名を設定する。
	*	リダイレクト先のURIパスを設定する。
	*	JSPに表示するデータをセットする。
*	返却値を返却する。


## 実装の仕方

何をやっているかを理解しながら進めることを意図し、少しずつ順を追って実装していきます。
下記の7つの段階に分けて進めていきます。

*	基本的な画面遷移を実装する。
*	フォーム(Java)にプロパティと妥当性検証ルールを実装する。
*	画面に入力フォーム(form要素)を実装する。
*	妥当性検証NGの場合の画面遷移パターンを実装する。
*	妥当性検証NGの場合のメッセージ表示を画面に実装する。
*	TODO登録画面の主たる業務ロジックである「DBにTODOレコードを作成する」を実装する。
*	作成したTODOレコードの内容を完了画面に表示する。


## フォーム

STEP 03では「基本的な画面遷移を実装する」こととします。

フォームについてはSTEP 03では枠のみ実装します。フォームの中身はSTEP 04で実装します。
なお、フォームクラスは設計書から生成しやすい部位ですので、実プロジェクトではコード生成されることが多いです。

フォームのコードは下記の通りです。
フォームは単純なBeanですので[Lombok](http://projectlombok.org "Lombok")を適用しやすいです。本チュートリアルでも使用します(クラスに付与した4つのアノテーションがそれです)。
また、フォームはセッションに格納することもあるので、シリアライザブルを実装 (`implements`) します。

```Java:TodoCreateForm
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class TodoCreateForm implements Serializable {

	private static final long serialVersionUID = 1L;

}
```


## コントローラ
### インタフェース
TODO登録画面に洗われる「入力画面」「確認画面」「登録処理」「完了画面」の4つに対応するメソッドを定義します。
また、入力フォームを初めて表示する時の初期値を明示するために、アノテーション`@ModelAttribute()`を指定したメソッドを定義します。

画面からの入力を受取るメソッドには引数としてフォーム (`TodoCreateForm form`) を受取るようにしてください。さらに、下記のように構成してください。これにより入力値を検証 (単項目チェック) できるようになります。

*	アノテーション`@Validated`を付与する。
*	直後の引数として`BindingResult binding`を受取るようにする。

これを踏まえ、インタフェースのコードは下記の通りです。

```Java:TodoCreateController
@RequestMapping(PathDef.URI_TODO_CREATE)
public interface TodoCreateController {

	@ModelAttribute()
	TodoCreateForm getForm();

	@RequestMapping()
	ModelAndView init(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request);

	@RequestMapping(PathDef.SUBURI_CONFIRM)
	ModelAndView confirm(@Validated TodoCreateForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request);

	@RequestMapping(PathDef.SUBURI_EXECUTE)
	ModelAndView execute(@Validated TodoCreateForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request, RedirectAttributes redirAttr);

	@RequestMapping(PathDef.SUBURI_FINISH)
	ModelAndView finish(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request,
			RedirectAttributes redirAttr);

}
```

### 実装クラス
「基本的な画面遷移」ということで、画面の表示とリダイレクトのみを実装します。

実装クラスのコードは下記の通りです。

```Java:TodoCreateControllerImpl
@Controller
public class TodoCreateControllerImpl implements TodoCreateController {

	@Override
	public TodoCreateForm getForm() {
		TodoCreateForm form = new TodoCreateForm();
		return form;
	}

	@Override
	public ModelAndView init(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE);
		return mav;
	}

	@Override
	public ModelAndView confirm(TodoCreateForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE_CONFIRM);
		return mav;
	}

	@Override
	public ModelAndView execute(TodoCreateForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request, RedirectAttributes redirAttr) {

		UriComponents uc = MvcUriComponentsBuilder.fromMethodName(
				TodoCreateController.class, PathDef.METHOD_FINISH, auth,
				locale, sitePref, request, redirAttr).build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(uc.toUriString(), true));
		return mav;
	}

	@Override
	public ModelAndView finish(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request,
			RedirectAttributes redirAttr) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE_FINISH);
		return mav;
	}

}
```

## JSP
「入力画面」「確認画面」「完了画面」のJSPを定義します。STEP 03では最小限にとどめます(どの画面か分かるように名前を記述しておきますが、後で実装する時に置き換えます)。

JSPのコードは下記の通りです。

```HTML:/WEB-INF/tiles/secure/todo/create/init.jsp
<h2>TODO登録</h2>
<p>入力画面</p>
```

```HTML:/WEB-INF/tiles/secure/todo/create/confirm.jsp
<h2>TODO登録</h2>
<p>確認画面</p>
```

```HTML:/WEB-INF/tiles/secure/todo/create/finish.jsp
<h2>TODO登録</h2>
<p>完了画面</p>
```

以上。
