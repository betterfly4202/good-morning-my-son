# 워크플로우

## 작업 흐름

```
1. 작업 시작
   - CLAUDE.md에서 현재 상태 확인
   - 오늘 할 작업 결정

2. 계획 수립
   - 복잡한 작업 → Plan Mode로 설계 먼저
   - 간단한 작업 → 바로 TodoWrite

3. 구현 (TodoWrite 추적)
   - 세부 작업 분해
   - 완료할 때마다 체크

4. 마무리
   - progress.md에 결과 기록
   - CLAUDE.md 상태 업데이트
   - 컨텍스트 길면 /compact
```

## 도구 활용

| 레벨 | 도구 | 용도 |
|------|------|------|
| 프로젝트 | CLAUDE.md | 전체 Phase 진행 상태 |
| 일간/세션 | docs/progress.md | 결정사항, 완료 작업 기록 |
| 작업 단위 | TodoWrite | 실시간 작업 추적 |
| 설계 | Plan Mode | 복잡한 기능 구현 전 설계 |

## Sub-Agent 활용

| Agent | 활용 시점 |
|-------|-----------|
| Explore | 코드베이스 탐색, "이 기능 어디 있어?" |
| Plan | 큰 기능 구현 전 설계, 아키텍처 결정 |
| Bash | Git 작업, 빌드, 테스트 실행 |

## 커밋 컨벤션

```
feat: 새로운 기능 추가
fix: 버그 수정
docs: 문서 변경
refactor: 코드 리팩토링
test: 테스트 추가/수정
```
