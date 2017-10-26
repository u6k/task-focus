# task-focus

[![Travis](https://img.shields.io/travis/u6k/task-focus.svg)](https://travis-ci.org/u6k/task-focus)
[![GitHub release](https://img.shields.io/github/release/u6k/task-focus.svg)](https://github.com/u6k/task-focus/releases)
[![license](https://img.shields.io/github/license/u6k/task-focus.svg)](https://github.com/u6k/task-focus/blob/master/LICENSE)
[![Docker Stars](https://img.shields.io/docker/stars/u6kapps/task-focus.svg)](https://hub.docker.com/r/u6kapps/task-focus/)
[![project-reports](https://img.shields.io/badge/site-project--reports-orange.svg)](https://u6k.github.io/task-focus/project-reports.html)
[![standard-readme compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=flat-square)](https://github.com/RichardLitt/standard-readme)

> 今日のタスク予定にフォーカスするアプリ「タスク・フォーカス」

ToDoリストやGTDはやるべきことを収集できますが、「今日やるべきことに注力」するには不向きです。カレンダーアプリは「時間が決まっている予定」を管理しやすいですが、細かい作業の管理には向きませんし、作業が早く終わった/作業が長引いたという状況に弱いです。タイムロガーは作業にかかった時間を記録することには向いていますが、予定の管理には向きません。

タスク・フォーカスは、 __「今日やるべきこと」__ に集中し、 __作業予定の変更に柔軟に対応__ できる、タスク管理アプリです。また、 __「今日どのようにすごしたか」__ を振り返ることができます。

## Table of Contents

<!-- TOC depthFrom:2 -->

- [Table of Contents](#table-of-contents)
- [Install](#install)
- [Usage](#usage)
- [Demo](#demo)
- [API](#api)
- [Maintainer](#maintainer)
- [Contribute](#contribute)
    - [前提条件](#前提条件)
    - [コマンドなど](#コマンドなど)
- [License](#license)

<!-- /TOC -->

## Install

Dockerイメージを配布しているため、次のように実行することができます。

```
$ docker run \
    -p 8080:8080 \
    u6kapps/task-focus
```

jarファイルを実行することもできます。

```
$ java -jar task-focus-x.x.x.jar
```

## Usage

TODO

## Demo

TODO

## API

TODO

## Maintainer

- [u6k - GitHub](https://github.com/u6k/)
- [u6k.Blog()](https://blog.u6k.me/)
- [u6k_yu1 | Twitter](https://twitter.com/u6k_yu1)

## Contribute

貴重なアイデアをご提案頂ける場合は、Issueを書いていただけると幸いです。あなたは、このプロジェクトに参加することによって、[Open Source Code of Conduct - thoughtbot](https://thoughtbot.com/open-source-code-of-conduct)を遵守することに同意します。

また、このアプリケーションはプラグイン形式で機能拡張することを想定しています。プラグインの開発は、ご自身のリポジトリで行ってください。

__TODO:__ プラグインの開発手順を説明

次に、このプロジェクトで開発を行う手順を説明します。

### 前提条件

- Java SDK

```
$ java -version
java version "1.8.0_144"
Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
Java HotSpot(TM) 64-Bit Server VM (build 25.144-b01, mixed mode)
```

- Docker

```
$ docker version
Client:
 Version:      17.07.0-ce
 API version:  1.31
 Go version:   go1.8.3
 Git commit:   8784753
 Built:        Tue Aug 29 17:41:05 2017
 OS/Arch:      windows/amd64

Server:
 Version:      17.09.0-ce
 API version:  1.32 (minimum version 1.12)
 Go version:   go1.8.3
 Git commit:   afdb6d4
 Built:        Tue Sep 26 22:45:38 2017
 OS/Arch:      linux/amd64
 Experimental: false
```

### コマンドなど

Eclipseプロジェクトを作成:

```
$ ./mvnw eclipse:eclipse
```

ユニット・テスト:

```
$ ./mvnw surefire-report:report
```

動作確認:

```
$ ./mvnw spring-boot:run
```

実行用Dockerイメージをビルド:

```
$ docker build -t task-focus .
```

実行:

```
$ docker run \
    -p 8080:8080 \
    task-focus
```

E2Eテスト:

__TODO:__ E2Eテストは未実装です。

## License

[MIT License &copy; 2016 u6k.apps@gmail.com](https://github.com/u6k/task-focus/blob/master/LICENSE)
