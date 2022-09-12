# server

### Git branch naming
```
master: 사용자에게 배포 가능한 상태만 관리하는 브랜치. release 이력 관리하기 위해 사용
develop: 다음 출시 버전을 개발하는 브랜치
feature: 기능을 개발하는 브랜치
    - feature/{기능-이름}
    ex) feature/login
release: 이번 출시 버전을 준비하는 브랜치
    - release-{버전}
hotfix: 출시 버전에서 발생한 버그를 수정하는 브랜치
    - hotfix-{버전}
```

### Git commit message
```text
Feat: 새로운 기능을 추가
Fix: 버그 수정
Design: CSS 등 사용자 UI 디자인 변경
!BREAKING CHANGE: 커다란 API 변경의 경우
!HOTFIX: 급하게 치명적인 버그를 고쳐야하는 경우
Style: 코드 포맷 변경, 세미 콜론 누락, 코드 수정이 없는 경우
Refactor: 프로덕션 코드 리팩토링
Comment: 필요한 주석 추가 및 변경
Docs: 문서 수정
Test: 테스트 코드, 리펙토링 테스트 코드 추가, Production Code(실제로 사용하는 코드) 변경 없음
Chore: 빌드 업무 수정, 패키지 매니저 수정, 패키지 관리자 구성 등 업데이트, Production Code 변경 없음
Rename:	파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우
Remove:	파일을 삭제하는 작업만 수행한 경우
```