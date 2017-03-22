# task-focus

あなたのタスク予定をナビゲートするアプリ「タスクナビ」

開発用Dockerイメージをビルド

```
docker build -t u6kapps/task-focus-webapp-dev -f Dockerfile-dev .
```

Eclipseプロジェクトを作成

```
docker run \
    --rm \
    -v $HOME/.m2:/root/.m2 \
    -v $(pwd):/var/my-app \
    u6kapps/task-focus-webapp-dev mvn eclipse:eclipse
```

テスト&パッケージング

```
docker run \
    --rm \
    -v $HOME/.m2:/root/.m2 \
    -v $(pwd):/var/my-app \
    u6kapps/task-focus-webapp-dev
```

実行用Dockerイメージをビルド

```
docker build -t u6kapps/task-focus-webapp .
```

実行

```
docker run \
    -d \
    --name task-focus-webapp \
    -p 8080:8080 \
    u6kapps/task-focus-webapp
```
