# task-focus

あなたのタスク予定をナビゲートするアプリ「タスクナビ」

開発用Dockerイメージをビルド

```
docker build -t task-focus-dev -f Dockerfile-dev .
```

Eclipseプロジェクトを作成

```
docker run \
    --rm \
    -v "${PWD}:/var/my-app" \
    -v "${HOME}/.m2:/root/.m2" \
    task-focus-dev mvn eclipse:eclipse
```

テスト&パッケージング

```
docker run \
    --rm \
    -v "${PWD}:/var/my-app" \
    -v "${HOME}/.m2:/root/.m2" \
    task-focus-dev
```

実行用Dockerイメージをビルド

```
docker build -t u6kapps/task-focus .
```

実行

```
docker-compose up -d
```
