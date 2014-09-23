Spring MVCチュートリアル
========================

# 概要
*	STEP 00: DBアクセスコードを生成する。
*	STEP 01: ログイン画面を作成する。


# STEP 00: DBアクセスコードを生成する。

DBアクセスにあたっては [MyBatis](http://mybatis.github.io/mybatis-3/ "MyBatis") をメインとして使用します。動的に条件を組立てるSQLを発行するケースでは [Querydsl](http://www.querydsl.com "Querydsl") (特に [Querydsl SQL](https://github.com/querydsl/querydsl/tree/master/querydsl-sql "Querydsl SQL") ) を使用します。
なお、DBアクセス用にコード生成ツールの [MyBatis Generator](http://mybatis.github.io/generator/ "MyBatis Generator") と [Querydsl codegen](https://github.com/querydsl/codegen "Querydsl codegen") を使用します。

```bash:コマンドライン
$ mvn clean compile flyway:migrate
$ mvn mybatis-generator:generate
$ mvn querydsl:export
```


# STEP 01: ログイン画面を作成する。



現在

以上。
