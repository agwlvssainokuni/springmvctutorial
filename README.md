Spring MVCチュートリアル
========================

# 概要
*	STEP 00: DBアクセスコードを生成する。
*	STEP 01: ホーム画面を作成する。
*	STEP 02: ログイン画面を作成する。


# STEP 00: DBアクセスコードを生成する。

DBアクセスにあたっては [MyBatis](http://mybatis.github.io/mybatis-3/ "MyBatis") をメインとして使用します。動的に条件を組立てるSQLを発行するケースでは [Querydsl](http://www.querydsl.com "Querydsl") (特に [Querydsl SQL](https://github.com/querydsl/querydsl/tree/master/querydsl-sql "Querydsl SQL") ) を使用します。
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
	*	JSP: `/WEB-INF/tiles/secure/home/init.jsp`
		*	ビュー名: `secure/home/init`

## コントローラ
### インタフェース
Spring MVC ではコントローラのインタフェースに「当該インタフェースが処理するURIパス」を、アノテーション `@RequestMapping(URIパス)` で定義します。アノテーションは、インタフェースとメソッドの両方に指定することができます。メソッドに指定したパスは、インタフェースに指定したURIパスからの相対パス (サブパス) として扱われます。
アノテーションに指定するURIパスは定数文字列である必要があります。実プロジェクトでは、URIパスは予め用意された定数定義を参照することが基本です。本チュートリアルでは、`PathDef` クラスに定数定義してありますので、これを参照します。

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

これに基づきインタフェースを下記の通り定義します (`package`や`import`は省略しています)。

```Java:HomeController
@RequestMapping(PathDef.URI_HOME)
public interface HomeController {

	@RequestMapping()
	ModelAndView init(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request);

}
```

### 実装クラス
コントローラインタフェースを実装 (`implements`) します。また、実装クラスにアノテーション `@Controller` を指定することで、Spring MVC が自動的にインスタンス登録します (Java以外に設定ファイルを記述する必要はありません)。
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
Spring MVC でも、ビューにJSPを使用する場合は、いわゆるJSPの記法でOKです。JSP, EL, [JSTL 1.2](https://jstl.java.net "JSTL 1.2"), Spring Tag, Spring Form Tag, Spring Security Tag が使えます。
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
	*	ログイン処理の実態は [Spring Security](http://docs.spring.io/spring-security/site/docs/3.2.5.RELEASE/reference/htmlsingle/)。
		*	ログイン成功: `/secure/`へリダイレクト。
		*	ログイン失敗: `/login?loginFailed`へリダイレクト。
		*	ログアウト: `/login?loggedOut`へリダイレクト
*	内部仕様
	*	コントローラ
		*	インタフェース: `cherry.spring.tutorial.web.login.LoginController`
		*	実装クラス: `cherry.spring.tutorial.web.login.LoginControllerImpl`
	*	JSP: `/WEB-INF/tiles/login/init.jsp`
		*	ビュー名: `login/init`
	*	ログイン成功/失敗、ログアウトのメッセージ表示
		*	フラッシュスコープを使用してメッセージを表示する。
		*	`/login?loginFailed`, `/login?loggedOut`のロジックの中で、フラッシュスコープにデータを入れて、`/login`へリダイレクトする。
		*	`/login`のロジックの中でフラッシュスコープのデータをJSPに受け渡すようにする。`/login`のJSPの中で受け渡されたデータに応じてメッセージの表示を切り替える。
		*	`/login`を再表示するとメッセージは再描画されない(フラッシュスコープの特性 = 一回こっきり)。

## コントローラ
### 概観
ログイン画面では、Spring MVC の下記の2つの技術要素を使用します。

*	URIパスが同じでも、リクエストパラメタを変えることで、呼び出されるメソッドを切り替えることができる。
*	フラッシュスコープを使い、リダイレクト元からリダイレクト先へデータを受渡すことができる。

#### 呼び出されるメソッドの切替え
アノテーション `@RequestMapping` の `params` を指定することで、当該メソッドが呼び出される条件にURIパス名にリクエストパラメタを追加することができます。
典型的には、`@RequestMapping(params = "パラメタ名=パラメタ値")`、`@RequestMapping(params = "パラメタ名")` という指定をします (`values = "URIパス"` も併記できます)。

昔ながらのフレームワークだと「一つの入力フォームにボタンを複数配置して、どれが押下されたかによってPOST先を切り替える」という制御を実装するには、「クライアントサイドでJavaScriptを使いform要素のonclickでaction属性を書替える」というやり方が使われていました。この制御が不要になります (input要素のボタンでなく、button要素を使います)。

#### フラッシュスコープ
リダイレクト元とリダイレクト先の両方のメソッドの引数に `RedirectAttributes redirAttr` を指定してください。
リダイレクト元のメソッドで、`redirAttr.addFlashAttribute()` を呼び出すことで、フラッシュスコープにデータを入れます。リダイレクト先のメソッドで、`redirAttr.getFlashAttributes()` を呼び出すことで、フラッシュスコープに入れて受け渡されたデータを取り出すことができます。フラッシュスコープで受け渡されたデータをJSPに渡すには `ModelAndView` の `addAllObjects` で `ModelAndView` にセットします。これらを合わせて、呼出しの典型パターンは `mav.addAllObjects(redirAttr.getFlashAttributes())` です。

### インタフェース
インタフェースは下記の通りです。

```Java:LoginController
@RequestMapping(PathDef.URI_LOGIN)
public interface LoginController {

	@RequestMapping()
	ModelAndView init(Locale locale, SitePreference sitePref,
			HttpServletRequest request, RedirectAttributes redirAttr);

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
			HttpServletRequest request, RedirectAttributes redirAttr) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_LOGIN);
		mav.addAllObjects(redirAttr.getFlashAttributes());
		return mav;
	}

	@Override
	public ModelAndView loginFailed(Locale locale, SitePreference sitePref,
			HttpServletRequest request, RedirectAttributes redirAttr) {
		redirAttr.addFlashAttribute("loginFailed", true);
		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(PathDef.URI_LOGIN, true));
		return mav;
	}

	@Override
	public ModelAndView loggedOut(Locale locale, SitePreference sitePref,
			HttpServletRequest request, RedirectAttributes redirAttr) {
		redirAttr.addFlashAttribute("loggedOut", true);
		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(PathDef.URI_LOGIN, true));
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

以上。
