# Mission Checker

## 목차
- [서비스 개요](#서비스-개요)
- [개발배경](#개발배경)
- [개발환경](#개발환경)

## 서비스 개요
미션을 수행한 뒤 미션 수행 결과를 표시하고 그것을 관리하는 서비스

## 개발배경
온라인으로 교육을 진행할 때 수강생들이 매일 수행해야 하는 미션이 있다.
현재 미션 수행 및 확인 프로세스는 수강생들이 미션 수행 후 카카오 단체 채팅방에 미션 수행 결과를 업로드하면
관리자가 확인하여 미션 진행 상황 표를 업데이트 하는 방식이다.
현재 방식은 관리자가 일일이 수강생의 미션 수행 여부를 확인하는 시간이 소모되는 점이 문제다.
따라서, 수동으로 진행되는 미션 수행 확인을 자동화 하는 것이 목표다.

## 개발환경
### 일반
- IntelliJ
- OpenJdk 11.0.13
- H2 Database
### 백엔드
- SpringBoot 2.7.1, Jpa, SpringDataJpa, Thymeleaf
### 프론트엔드
- Html/Css, Javascript, Bootstrap5

### 기능 구현 목록
- 기능 구현 목록의 접두어 M은 MVP에 해당하는 기능을 나타낸다.
- [ ] 회원가입
  - [x] M 일반적인 회원가입
  - [ ] SNS 계정을 이용하여 회원가입
- [ ] 회원
  - [x] M 모든 회원은 미션을 생성할 수 있다
  - [x] M 회원은 미션에 참여 신청할 수 있다
    - [ ] private 미션의 경우 참여 코드를 입력받을 수 있다
    - [ ] private 미션의 경우 외부에 노출하지 않을 수 있다
- [ ] 미션
  - [ ] 회원 등급별 최대 생성 개수 제한
  - [x] M 미션 생성시 미션 진행 시작일, 종료일을 지정할 수 있다
  - [x] M 미션 생성시 미션 수행 완료를 표시할 수 있는 일시(datetime) 조건을 지정할 수 있다
  - [ ] 미션 수행일에 공휴일 포함 여부도 지정할 수 있다
- [ ] 관리자
  - [x] M 관리자는 미션 참여 신청을 수락할 수 있다
    - [ ] 자동수락
    - [ ] 참여 인원 제한
  - [ ] 관리자는 미션 참여 신청을 거절할 수 있다
  - [ ] 관리자도 참여자로 미션 수행 완료 표시에 참여할 수 있다.
  - [ ] M 관리자는 모든 참여자의 미션 수행 완료 표시를 확인할 수 있다.
    - [ ] M 미션별로 체크 시트가 생성된다.
- [ ] 참여자
  - [x] M 참여자는 참여중인 미션에 수행 완료 표시를 남길 수 있다.
    - [x] M 과거 날짜도 수행 완료 표시를 남길 수 있어야 한다.
    - [x] M 이미 미션 수행 완료 표시된 날짜는 다시 표시할 수 없다.
    - [x] M 미션이 허용하는 시간대만에 미션 수행 완료 표시를 남길 수 있다.
  - [x] M 참여자는 본인의 미션 수행 완료 표시만 확인할 수 있다.
  