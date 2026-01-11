# /commit - 커밋 생성

변경사항을 검토하고 커밋을 생성합니다.

## 프로세스
1. `git status`로 변경사항 확인
2. `git diff`로 내용 검토
3. 최근 커밋 스타일 확인
4. 커밋 메시지 작성
5. 커밋 실행

## 커밋 메시지 규칙
```
<type>: <subject>

<body>

Co-Authored-By: betterfly <betterfly4202@gmail.com>
```

### Type
- `feat`: 새 기능
- `fix`: 버그 수정
- `refactor`: 리팩토링
- `docs`: 문서
- `test`: 테스트
- `chore`: 기타

## 규칙
- **푸시하지 않음** - 커밋만 생성
- **secrets 제외** - .env 등 민감 파일 확인
- **Task ID 포함** - 관련 task가 있으면 메시지에 포함
- 맥락이 다른 파일은 커밋을 나누어서 진행해야함
  - 이 부분이 모호할 경우 반드시 물어볼 것

## 사용 예시
```
/commit
/commit feat: T-001 자막 파서 구현
```
