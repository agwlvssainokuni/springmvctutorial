Spring MVCチュートリアル(2)
=========================

# 概要
*	STEP 10: TODO編集画面を作成する。(1)
*	STEP 11: TODO編集画面を作成する。(2)
*	STEP 12: TODO編集画面を作成する。(3)
*	STEP 13: TODO編集画面を作成する。(4)


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
	*	登録処理
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
	*	登録処理
		*	メソッド: `execute`
			*	パス変数に指定された主キー値を持つTODOレコードを照会する。
			*	引数として受渡されたフォームとバインド結果から入力値の妥当性を検証する。
			*	多重POSTを検証する。(ワンタイムトークン方式)
			*	入力値をフォームから取得し、DBのTODOレコードを更新する。
			*	楽観排他エラーを検証する。
			*	`ModelAndView`を作りリダイレクト先のURIパスを設定する。TODOレコードの主キー(id)はURIパスに埋込まれる。
			*	形成した`ModelAndView`を返却する。
		*	編集画面へHTTPリダイレクトする。


## 実装の仕方
TODO登録画面と同じように、何をしているか理解しながら進めることを意図し、少しずつ順を追って実装していきます。ただし、これまでに説明していない技術要素に注目します。既出の技術要素については説明を省略して少しスピードを上げます。
実装は下記の3つの段階に分けて進めていきます。

*	基本的な画面遷移を実装する。
*	入力および妥当性検証の画面遷移を作成する。
*	TODO編集画面の主たる業務ロジックである「DBのTODOレコードを更新する」を実装する。


# STEP 11: TODO編集画面を作成する。(2)

STEP 11では「基本的な画面遷移を実装」します。

## フォーム

```Java:TodoEditForm
```

## コントローラ

### インタフェース

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

```HTML:/WEB-INF/tiles/secure/todo/edit/init.jsp
<h2>TODO編集</h2>
```


# STEP 12: TODO編集画面を作成する。(3)

STEP 12では「画面の入力および妥当性検証の画面遷移を作成」します。

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

# STEP 13: TODO編集画面を作成する。(4)

STEP 13では「TODO編集画面の主たる業務ロジックである「DBのTODOレコードを更新する」を実装」します。

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
