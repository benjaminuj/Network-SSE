# Network-SSE
Server-Sent-Events를 이용한 실시간 통신


# 🖥️ 프로젝트 소개
- Server-Sent-Events를 이용한 실시간 채팅 기능을 구현해요
- 기술적 고민
    - SSE 실시간 통신에서 발생할 수 있는 문제 시나리오를 예상해서 그려요
    - 문제를 해결해요
<br>

## 🕰️ 개발 기간
* 2024.12.11 - 진행중

## ⚙️ 개발 환경
- `Java 21`
- **Framework** : `Springboot (3.4)`
- **Database** : `MySQL (9.0.1)`
- **ORM** : `JPA`

## 🔥 문제 시나리오

### 1. 클라이언트 연결이 비정상적으로 끊어졌을 때 서버에서 데이터를 보내면?

- `Last-Event-ID`를 기반으로 끊어진 동안 전송된 메시지를 재전송하는 로직을 구현해서 해결했어요
- 미수신 메시지의 순서를 보장하기 위해, event id값에 생성시간을 넣었어요

https://github.com/user-attachments/assets/677522a0-ebda-4021-8985-eb3b45706f4d
