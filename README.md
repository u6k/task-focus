# task-focus

あなたのタスク予定をナビゲートするアプリ「タスクナビ」

開発用Dockerイメージをビルド

```
docker build -t task-focus-webapp-dev -f Dockerfile-dev .
```

Eclipseプロジェクトを作成

```
docker run \
    --rm \
    -v "${HOME}/.m2:/root/.m2" \
    -v "${PWD}:/var/my-app" \
    task-focus-webapp-dev mvn eclipse:eclipse
```

テスト&パッケージング

```
docker run \
    --rm \
    -v "${HOME}/.m2:/root/.m2" \
    -v "${PWD}:/var/my-app" \
    task-focus-webapp-dev
```

実行用Dockerイメージをビルド

```
docker build -t u6kapps/task-focus-webapp .
```

実行

```
docker run \
    --rm \
    -p "8080:8080" \
    u6kapps/task-focus-webapp
```
