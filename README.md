Spring MVCチュートリアル
========================

# 概要
*	準備
*	STEP 00: DBアクセスコードを生成する。
*	STEP 01: ホーム画面を作成する。
*	STEP 02: ログイン画面を作成する。
*	STEP 03: TODO登録画面を作成する。(1)
*	STEP 04: TODO登録画面を作成する。(2)
*	STEP 05: TODO登録画面を作成する。(3)
*	STEP 06: TODO登録画面を作成する。(4)
*	STEP 07: TODO登録画面を作成する。(5)
*	STEP 08: TODO登録画面を作成する。(6)
*	STEP 09: TODO登録画面を作成する。(7)

# 準備
## 開発者環境のセットアップ

*	下記のツールをセットアップしてください。
	*	Git
	*	Gradle
	*	Maven3
*	WildFly (またはJBoss EAP。以下、WildFlyと総称します) をセットアップしてください。
	*	standalone-full.xml を使用します。
	*	データソースを作成してください。
		*	JDBCドライバ: H2 (WildFlyに標準搭載)
		*	JNDI名: java:/datasources/TutorialApp
		*	URL: jdbc:h2:mem:TutorialApp
		*	DBユーザ: sa (パスワードは指定しない)
*	Eclipseをセットアップしてください。
	*	Eclipse Luna (4.4)
	*	マーケットプレースから JBoss Tool または JBoss Developer Studio をインストールしてください。
	*	先にセットアップしたWildFlyを指定してサーバを作成してください。standalone-full.xmlを指定してください。

## チュートリアルのセットアップ

*	本チュートリアルをcloneしてください。
*	下記のコマンドでEclipseプロジェクト化してください。

	```bash:コマンドライン
	$ cd springmvctutorial
	$ gradle eclipse
	```

*	本チュートリアルのプロジェクトを、Eclipseでインポートしてください。
*	また、本チュートリアルのプロジェクトを、EclipseでWildFlyサーバに追加してください。

## セットアップの確認

*	EclipseでWildFlyサーバを開始してください。
*	ブラウザで下記URLにアクセスしてください。
	*	http://localhost:8080/springmvctutorial/
	*	まだ中身を作成していないので何も表示されませんが、http://localhost:8080/springmvctutorial/secure/ へHTTPリダイレクトされればOKです。
*	WildFlyサーバを停止してください。

## チュートリアルの進め方

*	以下のような進め方を想定して構成しています。
	*	step-00から順に進める。
	*	まず説明を読む。
	*	説明に従い、実際に自分の手でプログラムを書いてみる。
	*	自分で書いたプログラムをWildFlyサーバで動かして動作を確認する。
	*	`git checkout step-{NN}` ({NN}は各ステップの番号) すると、当該ステップのプログラム例を見ることができる。
		*	自分で書いてみる用とプログラム例を見る用とで、ワーキングコピーを別々に展開しておくのがオススメです。
	*	次のステップへ進む。


# STEP 00: DBアクセスコードを生成する。

DBアクセスにあたっては [MyBatis](http://mybatis.github.io/mybatis-3/ "MyBatis") をメインとして使用します。動的に条件を組立てるSQLを発行するケースでは [Querydsl](http://www.querydsl.com "Querydsl") (特に [Querydsl SQL](https://github.com/querydsl/querydsl/tree/master/querydsl-sql "Querydsl SQL")) を使用します。
なお、DBアクセス用にコード生成ツールの [MyBatis Generator](http://mybatis.github.io/generator/ "MyBatis Generator") と [Querydsl codegen](https://github.com/querydsl/codegen "Querydsl codegen") を使用します。

```bash:コマンドライン
$ mvn clean process-resources flyway:migrate
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
Spring MVC ではコントローラのインタフェースに「当該インタフェースが処理するURIパス」を、アノテーション`@RequestMapping(URIパス)`で定義します。アノテーションは、インタフェースとメソッドの両方に指定することができます。メソッドに指定したパスは、インタフェースに指定したURIパス配下の子パス (サブパス) として扱われます。
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
		*	`long pageNo`
		*	`long pageSz`
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

リダイレクト先URIは文字列で指定します。URIパスの特定にあたっては、Spring MVC のAPIの`MvcUriComponentsBuilder`の`fromMethodCall()`メソッドを使用してください。引数はリダイレクト先のメソッド呼出しを表現するオブジェクト1つです。`MvcUriComponentsBuilder.on(XxxController.class)`でスタブオブジェクトを作り、同オブジェクトのメソッド (リダイレクト先のメソッド) を呼出し、その返却値を前記`fromMethodCall()`メソッドの引数として受け渡してください。単体テストに使う[Mockito](https://code.google.com/p/mockito/ "Mockito")をイメージすると良いでしょう。
リダイレクト先にパス変数が含まれる場合は、スタブオブジェクトのメソッド呼出しで指定した値を該当箇所に埋込んだ形でURIを形成してくれます。

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

		UriComponents redirTo = fromMethodCall(
				on(LoginController.class).init(locale, sitePref, request))
				.build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(redirTo.toUriString(), true));
		return mav;
	}

	@Override
	public ModelAndView loggedOut(Locale locale, SitePreference sitePref,
			HttpServletRequest request, RedirectAttributes redirAttr) {

		redirAttr.addFlashAttribute("loggedOut", true);

		UriComponents redirTo = fromMethodCall(
				on(LoginController.class).init(locale, sitePref, request))
				.build();

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

# STEP 03: TODO登録画面を作成する。(1)

いよいよ入力フォームを持つ画面を作成します。作成するものは、これまでと同様の「コントローラ」と「JSP」に加え、入力値を保持する「フォーム」です。

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
		*	URIパス: `/secure/todo/create/finish`
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
			*	多重POSTを検証する。(ワンタイムトークン方式)
			*	入力値をフォームから取得し、DBにTODOレコードを作成する。
			*	`ModelAndView`を作りリダイレクト先のURIパスを設定する。作成したレコードの主キー(id)をリクエストパラメタとして受渡す。
			*	形成した`ModelAndView`を返却する。
		*	完了画面へHTTPリダイレクトする。
	*	完了画面
		*	メソッド: `finish`
			*	リクエストパラメタに指定された主キー(id)に該当するTODOレコードをDB照会する。
			*	`ModelAndView`を作りビュー名を設定する。また、`ModelAndView`にTODOレコードを設定する。
			*	形成した`ModelAndView`を返却する。
		*	JSP: `/WEB-INF/tiles/secure/todo/create/finish.jsp` (ビュー名: `secure/todo/create/finish`)

## コントローラのメソッドの基本構成
コントローラのメソッドは原則として下記の構成をとるようにしてください。なお、処理の内容に応じて実施しないこともあり得ます。上記に記述したメソッドの内部仕様もこれに則っています。

*	入力値の妥当性を検証する。
	*	単項目チェック (入力値の字面上の様式をチェックする)。
	*	項目間チェック (複数項目に亘る依存性に基づいて入力値をチェックする)。
	*	データ整合性チェック (マスタテーブル、区分値、設定値との整合性をチェックする)。
	*	多重POSTチェック (ワンタイムトークンを照合する)
*	主たる業務ロジックを実行する。
	*	DBの照会や更新処理 (CRUD)。
	*	メッセージキューの操作 (非同期処理を実行登録)。
	*	フラッシュスコープにデータをセットする。
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

STEP 03では「基本的な画面遷移を実装」します。

フォームについてはSTEP 03では枠のみ実装します。フォームの中身はSTEP 04で実装します。
なお、フォームクラスは設計書から生成しやすい部位ですので、実プロジェクトではコード生成されることが多いでしょう。

フォームのコードは下記の通りです。
フォームは単純なBeanですので[Lombok](http://projectlombok.org "Lombok")を適用しやすいと言えます。本チュートリアルでも使用しています。クラスに付与した4つのアノテーションがそれです。
また、フォームはセッションに格納することもあるので、`Serializable`を実装 (`implements`) します。

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
			HttpServletRequest request);

	@RequestMapping(PathDef.SUBURI_FINISH)
	ModelAndView finish(@RequestParam(PathDef.PATH_VAR_ID) int id,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request);

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
			HttpServletRequest request) {

		Integer id = 0;

		UriComponents uc = fromMethodCall(
				on(TodoCreateController.class).finish(id, auth, locale,
						sitePref, request)).build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(uc.toUriString(), true));
		return mav;
	}

	@Override
	public ModelAndView finish(int id, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {
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


# STEP 04: TODO登録画面を作成する。(2)

STEP 04では「フォーム(Java)にプロパティと妥当性検証ルールを実装」します。

## フォームの仕様
フォームの仕様は下記の通りです。

*	TODOの期日
	*	パラメタ名: `dueDate`
	*	データ型: 日付 `LocalDate` ([Joda-Time](http://www.joda.org/joda-time/ "Joda-Time"))
	*	検証条件
		*	必須
	*	初期値: 7日後 (設定値)
*	TODOの内容
	*	パラメタ名: `description`
	*	データ型: 文字列 (`String`)
	*	検証条件
		*	必須
		*	最大5000文字

## フォームの実装
フォームは、「Beanとしてのプロパティの定義」と「妥当性検証ルールのアノテーション指定」の2つの要素からなります。

Spring MVCはアノテーションによる妥当性検証([JSR 303: Bean Validation](https://jcp.org/en/jsr/detail?id=303 "JSR 303: Bean Validation"), [JSR 349: Bean Validation 1.1](https://jcp.org/en/jsr/detail?id=349 "JSR 349: Bean Validation 1.1"), [Hibernate Validator](http://hibernate.org/validator/ "Hibernate Validator"))に対応しています。基本的に標準で提供されているアノテーションを使いますが、案件によっては自前で妥当性検証のアノテーションを定義して使います。本チュートリアルでは、標準の`@Range`を元にして、最大長チェックのアノテーション`@MaxLength`を定義しています。TODO登録のフォームでもこれを使用します。

昔ながらのフレームワークでは文字列で受取りますが、Spring MVCでは型変換(`ConversionService`)の仕組みを使うことで、文字列以外のプロパティで入力値を受取ることができます。プロパティの型に変換できない場合は「形式不正(typeMismatch)で妥当性検証NG」と判定されます。また、変換元の入力値は`BindingResult`が持っており、入力画面で再入力を促す際も「元の入力値」が画面に表示されます。

以上を踏まえ、フォームを以下の通り定義します。
必須判定を意図してアノテーション`@NotNull`と`@NotEmpty`を指定しています。この使い分けは、文字列は`@NotEmpty`、文字列以外は`@NotNull`です。

```Java:TodoCreateForm
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class TodoCreateForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private LocalDate dueDate;

	@NotEmpty
	@MaxLength(5000)
	private String description;

}
```

## 初期値の構成
コントローラのインタフェースでアノテーション`@ModelAttribute()`を付与したメソッド`TodoCreateForm getForm()`が返却する値が、フォームの初期値として使用されます。
具体的な初期値の構成方法を下記に示します。

フォームの「期日」の初期値の仕様は、前述の通り「7日後」です。値の算出式は「『今日の日付』+『7日(設定値)』」ですので、以下の手順で処理します。

*	初期値「期日」の「オフセット」を設定ファイルから詠込む。
*	「今日の日付」を取得する。
*	「『今日の日付』+『オフセット』」を算出する。

### 初期値「期日」の「オフセット」を設定ファイルから詠込む
昔ながらのフレームワークでは、設定ファイルから「設定値を取得する」という「処理」をコーディングしました。しかし、Spring FrameworkではDI (Dependency Injection) の機構によって、「値を注入させる」という方式をとります。業務ロジックのコードが実行される時点で、既に「設定値がインスタンス変数に入っている」状態です。「設定値を取得する」という処理をコーディングする必要はありません。

具体的には、Spring Frameworkの`PropertyPlaceholderConfigurer` (アプリケーションコンテキスト設定ファイルの`<context:property-placeholder />`タグと等価) を使用します。これにより「インスタンス変数にアノテーション`@Value("${プロパティのキー名}")`を付与することで、プロパティファイル (.properties) の設定値が注入される」ようになります。

以上を踏まえ、プロパティファイルとコントローラの実装クラスを、以下の通り実装します。

```Init:tutorial.properties
tutorial.web.secure.todo.create.defaultOffsetOfDueDate=7
```

```Java:TodoCreateControllerImpl
	@Value("${tutorial.web.secure.todo.create.defaultOffsetOfDueDate}")
	private int defaultOffsetOfDueDate;
```

### 「今日の日付」を取得する
要件にも依存しますが、業務システムにおいては通常「締め」という考え方があります。このため、システム日時とは別に「業務処理の上では現在はxx年xx月xx日として扱う」ことが一般的です。これを「業務日付」や「業務日時」と呼び、システム日時と区別します。

本チュートリアルもこの考え方に則り、業務日付を今日の日付とします。業務日付は`BizDateTime#today()`メソッドで取得します。

### 「『今日の日付』+『オフセット』」を算出する
`BizDateTime#today()`メソッドを呼出すために、コントローラの実装クラスに`BizDateTime`のインスタンスが注入されるように構成します。具体的には、インスタンス変数`private BizDateTime bizDateTime`を定義し、これにアノテーション`@Autowired`を付与します。

以上を踏まえ、コントローラの実装クラスを、以下の通り実装します。

```Java:TodoCreateControllerImpl
	@Autowired
	private BizDateTime bizDateTime;

	@Override
	public TodoCreateForm getForm() {
		TodoCreateForm form = new TodoCreateForm();
		form.setDueDate(bizDateTime.today().plusDays(defaultOffsetOfDueDate));
		return form;
	}
```


# STEP 05: TODO登録画面を作成する。(3)

STEP 05では「画面に入力フォーム(form要素)を実装」します。

## JSP

「入力画面」に入力フォームを表示します。

```HTML:/WEB-INF/tiles/secure/todo/create/init.jsp
<h2>TODO登録</h2>
<f:form servletRelativeAction="/secure/todo/create/confirm"
	method="POST" modelAttribute="todoCreateForm" role="form">
	<div class="form-group">
		<f:label path="dueDate" cssErrorClass="has-error">期日</f:label>
		<f:input path="dueDate" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<div class="form-group">
		<f:label path="description" cssErrorClass="has-error">内容</f:label>
		<f:textarea path="description" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<f:button type="submit" class="btn btn-default">確認</f:button>
</f:form>
```

また、「確認画面」に入力値を再表示するととみに、後続の「登録処理」に受渡すための隠し入力フォームを配置します。

更新系の処理を実行する際は、「ワンタイムトークンを照合」するkとで、多重POST (具体的には、誤って「登録」ボタンを二回以上押下してしまう誤操作) による意図しない更新を抑止します。この制御のために、JSP側でワンタイムトークンの払出しと`<input type="hidden" />`タグ埋込み、サーバ側で払出されたワンタイムトークンの照合を実装します。具体的には、JSP側では隠し入力フォームに`<mytag:onetimetoken />`を配置します。

```HTML:/WEB-INF/tiles/secure/todo/create/confirm.jsp
<h2>TODO登録</h2>
<s:nestedPath path="todoCreateForm">
	<div class="form-group">
		<f:label path="dueDate">期日</f:label>
		<f:input path="dueDate" cssClass="form-control" readonly="true" />
	</div>
	<div class="form-group">
		<f:label path="description">内容</f:label>
		<f:textarea path="description" cssClass="form-control" readonly="true" />
	</div>
</s:nestedPath>
<f:form servletRelativeAction="/secure/todo/create/execute"
	method="POST" modelAttribute="todoCreateForm" id="todoCreateFormHidden">
	<f:hidden path="dueDate" id="dueDateHidden" />
	<f:hidden path="description" id="descriptionHidden" />
	<f:button type="submit" class="btn btn-default">登録</f:button>
	<mytag:onetimetoken />
</f:form>
```


# STEP 06: TODO登録画面を作成する。(4)

STEP 06では「妥当性検証NGの場合の画面遷移パターンを実装」します。

## コントローラ
ブラウザからPOSTされたデータは「全てのデータ値の妥当性を検証」します。
隠し入力フォームでデータをPOSTする場合も、「確認画面」を表示する際に妥当性検証されてはいますが、不正な操作でPOSTされる危険性を排除することはできないので、同じ妥当性検証を実施しなければなりません。なお、多重POSTチェックだけは実際の更新処理が発生する「登録処理」でのみ実施します。

入力値の妥当性の検証のうち「単項目チェック (入力値の字面上の様式をチェック)」については、Spring MVCの標準のバリデーション機構により検証します。Spring MVCは、フォームに指定したアノテーションに従い入力値を検証し、その結果が`BindingResult`にセットされて、コントローラのメソッドに渡されます。検証NGか否かは`BindigResult#hasErrors()`メソッドで判定します。検証NGのメッセージは、JSPタグ`<s:errors />`を使用して画面に表示します。メッセージを形成するために、コントローラ側で実施する処理は特にありません。

入力値の妥当性の検証のうち「項目間チェック (複数項目に亘る依存性に基づいて入力値をチェック)」「データ整合性チェック (マスタテーブル、区分値、設定値との整合性をチェック)」「多重POSTチェック (ワンタイムトークンを照合)」については、業務ロジックとして個々に実装します。ただし、画面に検証NGのメッセージを表示する仕組みはSpring MVCに則ります。具体的には、`BindingResult`に入力エラー情報をセットします。
`BindingResult`へは画面に表示するメッセージのID(コード)をセットします。Spring MVCは、プロパティファイル(`message/error.properties`, `message/preseterror.properties`)から、セットされたID(コード)に対応するメッセージの文言を取得し画面に表示します。
`BindingResult`のメソッドを直接呼出してもメッセージのID(コード)をセットすることはできますが、原則として、補助機能として提供されている`LogicalErrorUtil`を使用します。

ワンタイムトークンの照合は補助機能として提供されている`OneTimeTokenValidator`を使用します(Spring MVCの標準APIとしては提供されていません)。

以上を踏まえ、下記の通りコントローラを実装します。

### 実装クラス
補助機能`OneTimeTokenValidator`を注入します。

```Java:TodoCreateControllerImpl
	@Autowired
	private OneTimeTokenValidator oneTimeTokenValidator;
```

「確認画面」を表示する際に入力値の妥当性を検証します。検証NGの場合は「入力画面」を表示します。

```Java:TodoCreateControllerImpl#confirm
	@Override
	public ModelAndView confirm(TodoCreateForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request) {

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE);
			return mav;
		}

		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE_CONFIRM);
		return mav;
	}
```

「登録処理」を実行する際も入力値の妥当性を検証します。検証NGの場合は「入力画面」を表示します(直前の「確認画面」に遷移する訳では無い点に注意してください)。
また、「登録処理」では実際の更新処理が発生しますので、ワンタイムトークンを照合します。照合NGの場合は、エラーメッセージの情報を`BindingResult`にセットし、「入力画面」を表示します。

```Java:TodoCreateControllerImpl#execute
	@Override
	public ModelAndView execute(TodoCreateForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request) {

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE);
			return mav;
		}

		if (!oneTimeTokenValidator.isValid(request)) {
			LogicalErrorUtil.reject(binding, LogicalError.OneTimeTokenError);
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE);
			return mav;
		}

		Integer id = 0;

		UriComponents uc = fromMethodCall(
				on(TodoCreateController.class).finish(id, auth, locale,
						sitePref, request)).build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(uc.toUriString(), true));
		return mav;
	}
```


# STEP 07: TODO登録画面を作成する。(5)

STEP 07では「妥当性検証NGの場合のメッセージ表示を画面に実装」します。

## JSP
妥当性検証NGの情報は、コントローラから引渡された`BindingResult`に格納されています。JSPからはタグを使用して照会します。

*	`<s:hasBindErrors name="フォーム名">...</s:hasBindErrors>`: エラーがある場合、タグの内側の内容を画面に表示します。
*	`<s:errors path="フォーム名" />`: フォームそのもののエラーメッセージを表示する。
*	`<s:errors path="フォーム名.プロパティ名" />`: フォームのプロパティのエラーメッセージを表示する。
*	`<s:nestedPath path="フォーム名">...</s:nestedPath>`: このタグの内側では、`<s:errors path="フォーム名.プロパティ名" />`を`<:errors path="プロパティ名" />`の形で記述できる(フォーム名を省ける)。

これらを使用し、下記のようにJSPを実装します。

```HTML:/WEB-INF/tiles/secure/todo/create/init.jsp
<h2>TODO登録</h2>
<s:hasBindErrors name="todoCreateForm">
	<div class="form-group has-error">
		<div class="help-block bg-danger">
			<f:errors path="todoCreateForm" element="div" />
			<s:nestedPath path="todoCreateForm">
				<f:errors path="dueDate" element="div" />
				<f:errors path="description" element="div" />
			</s:nestedPath>
		</div>
	</div>
</s:hasBindErrors>
<f:form servletRelativeAction="/secure/todo/create/confirm"
	method="POST" modelAttribute="todoCreateForm" role="form">
	<div class="form-group">
		<f:label path="dueDate" cssErrorClass="has-error">期日</f:label>
		<f:input path="dueDate" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<div class="form-group">
		<f:label path="description" cssErrorClass="has-error">内容</f:label>
		<f:textarea path="description" cssClass="form-control"
			cssErrorClass="form-control has-error" />
	</div>
	<f:button type="submit" class="btn btn-default">確認</f:button>
</f:form>
```

## message/form.propertiesファイル (入力項目の表示名)
入力項目の表示名を「フォーム名.プロパティ=表示名」の形式でプロパティファイルを作成し、これをSpring MVCに読込ませるせることで、画面に表示されるようになります。フォームクラスを設計書から生成する場合は、項目の表示名のプロパティファイルも一緒に生成することが多いです。
本チュートリアルでは、`message/form.properties`に定義します。

```Ini:message/form.properties
todoCreateForm.dueDate=期日
todoCreateForm.description=内容
```


# STEP 08: TODO登録画面を作成する。(6)

STEP 08では「TODO登録画面の主たる業務ロジックである「DBにTODOレコードを作成する」を実装」します。

## クラス構成
### サービス
「サービス」という層を設け、コントローラのメソッドからサービスのメソッドを呼出す構成とします。サービスの位置づけは「業務ロジックの処理の本体」です。典型的には、サービスのメソッドがトランザクションの単位となります。

### DAO (Data Access Object)
実際のプロジェクトでは、DAO (Data Access Object) という層を設けてDBアクセスを抽象化することが一般的です。これにより、DBアクセス処理の詳細を隠蔽し、複数の機能から共用しやすくする、また、変更が発生した場合に変更箇所を局所化するという効果を期待します。
サービスはDAOを呼出してDBアクセスします。

本チュートリアルでは、Spring MVCフレームワークの使い方を把握することを主眼とし、DAOによる抽象化は行いません。サービスのメソッドの中で、直接DBアクセスフレームワーク (MyBatis, Querydsl SQL) を呼出します。
実際のプロジェクトでは、プロジェクトの規約に従ってください。


## DBアクセスの仕方
### DBアクセスフレームワーク
DBアクセスにあたっては [MyBatis](http://mybatis.github.io/mybatis-3/ "MyBatis") をメインとして使用します。動的に条件を組立てるSQLを発行するケースでは [Querydsl](http://www.querydsl.com "Querydsl") (特に [Querydsl SQL](https://github.com/querydsl/querydsl/tree/master/querydsl-sql "Querydsl SQL")) を使用します。特に、コード生成ツール[MyBatis Generator](http://mybatis.github.io/generator/ "MyBatis Generator"), [Querydsl codegen](https://github.com/querydsl/codegen "Querydsl codegen")で生成したプログラムを使用します。
TODO登録画面では、動的なSQLを必要としないため、MyBatisを使用します。

### MyBatisの構成
MyBatisはツールで生成した下記の構成要素を使用します。これらはテーブルごとに1セットずつ生成されます。

*	DTO (Data Transfer Object): テーブルのレコードのデータを保持するBeanです。Mapperインタフェースのメソッドの引数や返却値として使用されます。
*	Mapperインタフェース: テーブルの基本的なCRUDをメソッドとして提供します。
*	Criteriaクラス: RUDの検索条件 (WHERE句) を表現します。
*	SQLプロバイダ: 実際に発行されるSQLを組立てます。Mapperインタフェースを呼出すと、間接的にSQLプロバイダが呼出されます。通常は、SQLプロバイダを直接呼出すことはありません。

### トランザクション制御
メソッドにアノテーション`@Transactional`を指定することで、当該メソッドがトランザクション制御の単位となります。これはSpring Frameworkの宣言的トランザクション制御の仕組みを使用しています。なお、アノテーションは実装クラス側のメソッドに指定してください。インタフェースのメソッドに指定してもトランザクション制御されません。

アノテーション`@Transactional`を指定したメソッドの中で、アノテーション`@Transactional`を指定した別のメソッドを呼出すことも可能です。呼出されたメソッドがどのようにトランザクション制御されるかは、アノテーションに指定した`propagation`属性に依存します。特に指定しなければ、呼出し元と同じトランザクションとして実行されます。

アノテーション`@Transactional`は、`readOnly`という属性を持っています。処理が参照のみの場合は`@Transactional(readOnly=true)`と指定してください。更新系処理を含むトランザクションはマスタ、参照系処理のみのトランザクションはレプリカへDB接続するよう振分ける場合に、`readOnly`の指定が判断基準となります。


## サービス
下記の通りサービスを実装します。

### インタフェース

```Java:TodoService
public interface TodoService {

	Integer create(Todo todo);

}
```

### 実装クラス

```Java:TodoServiceImpl
@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoMapper todoMapper;

	@Transactional
	@Override
	public Integer create(Todo todo) {
		int count = todoMapper.insertSelective(todo);
		if (count != 1) {
			return null;
		}
		return todo.getId();
	}

}
```

## コントローラ
下記の通りコントローラを実装します。

### サービスをDIする

```Java:TodoCreateControllerImpl
	@Autowired
	private TodoService todoService;
```

### 「登録処理」にサービス呼出しを追加する

```Java:TodoCreateControllerImpl
	@Override
	public ModelAndView execute(TodoCreateForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request) {

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE);
			return mav;
		}

		Todo todo = new Todo();
		todo.setPostedBy(auth.getName());
		todo.setPostedAt(bizDateTime.now());
		todo.setDueDate(form.getDueDate());
		todo.setDescription(form.getDescription());
		Integer id = todoService.create(todo);
		if (id == null) {
			throw new IllegalStateException("Failed to create todo record: "
					+ todo.toString());
		}

		UriComponents uc = fromMethodCall(
				on(TodoCreateController.class).finish(id, auth, locale,
						sitePref, request)).build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(uc.toUriString(), true));
		return mav;
	}
```


# STEP 09: TODO登録画面を作成する。(7)

STEP 09では「作成したTODOレコードの内容を完了画面に表示」します。

## サービス
登録したTODOレコードを照会するDBアクセス処理を作成します。登録した利用者以外は参照できないよう条件を組立てます。また、削除フラグが立っていない (= 論理削除されていない) という条件も指定します。

### インタフェース

```Java:TodoService
	Todo findById(String loginId, int id);
```

### 実装クラス

```Java:TodoServiceImpl
	@Transactional(readOnly = true)
	@Override
	public Todo findById(String loginId, int id) {
		TodoCriteria crit = new TodoCriteria();
		Criteria c = crit.createCriteria();
		c.andIdEqualTo(id);
		c.andPostedByEqualTo(loginId);
		c.andDeletedFlgEqualTo(DeletedFlag.NOT_DELETED);
		List<Todo> list = todoMapper.selectByExample(crit);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
```

## コントローラ
サービスを呼出してTODOレコードのデータを取得し、有為なデータが得られたらJSPに受渡します。

下記のコードでは`ModelAndView#addObject(Object)`メソッドを使用しています。同メソッドを使用すると、JSPから参照する際のオブジェクト名を一定の規則に従って自動で生成してくれます。
オブジェクト名の生成ルールは下記の通りです。

*	コレクション以外: クラス名のシンプル名(= パッケージ名なし)の先頭を小文字に変換した名前。例: camelCase
*	コレクション: コレクションが保持するクラスから名前を決定し、その後ろにコレクション名を追加した名前。例: camelCaseList, camelCaseSet

明示的にオブジェクト名を指定する`addObject(String, Object)`メソッドもあります。必要に応じて使い分けてください。
基本的に`addObject(Object)`を使用することとし、クラス名から生成した名前がデータの意味を十分に表さない場合は`addObject(String, Object)`を使用するという使い分けが典型的でしょう (例えば、`Todo todo`ならば`addObject(todo)`、`Integer id`ならば`addObject("id", id)`)。
本チュートリアルでもそのように使い分けます。

### 実装クラス

```Java:TodoCreateControllerImpl
	@Override
	public ModelAndView finish(int id, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE_FINISH);
		Todo todo = todoService.findById(auth.getName(), id);
		if (todo != null) {
			mav.addObject(todo);
		}
		return mav;
	}
```

## JSP
コントローラから受渡されたTODOレコードを画面に表示します。
表示の仕方は様々なバリエーションがありますが、ここでは「入力画面」「確認画面」「完了画面」の見た目の印象を揃えることを意図し入力フォームと同じ部品(`<f:input />`や`<f:textarea />`)を使用しています。

```HTML:/WEB-INF/tiles/secure/todo/create/finish.jsp
<h2>TODO登録</h2>
<s:nestedPath path="todo">
	<div class="form-group">
		<f:label path="dueDate">期日</f:label>
		<f:input path="dueDate" cssClass="form-control" readonly="true" />
	</div>
	<div class="form-group">
		<f:label path="description">内容</f:label>
		<f:textarea path="description" cssClass="form-control" readonly="true" />
	</div>
	<div class="form-group">
		<f:label path="id">TODO番号</f:label>
		<f:input path="id" cssClass="form-control" readonly="true" />
	</div>
	<div class="form-group">
		<f:label path="postedAt">登録日時</f:label>
		<f:input path="postedAt" cssClass="form-control" readonly="true" />
	</div>
</s:nestedPath>
```

なお、部品を使うことで、日付データ、日時データが自動的に統一書式で整形されます。これも部品を使用した意図の一つです。
部品を使わない場合は、`<s:bind path="dueDate">${status.value}</s:bind>`という書き方をすることで、統一書式で整形することができます。

以上。
