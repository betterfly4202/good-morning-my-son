# /verify - 구현 검증

spec 기준으로 구현이 올바른지 검증합니다.

## 입력
- spec 파일 경로 (예: `.claude/features/xxx/spec.md`)
- 검증 대상 코드/기능

## 프로세스

```
┌─────────────────────────────────────────┐
│  1. Spec의 Success Criteria 추출        │
│  2. 각 기준별 검증 수행                 │
│  3. 결과 보고                           │
└─────────────────────────────────────────┘
```

## 규칙
- **spec 기준** - spec에 명시된 것만 검증
- **객관적 판단** - Pass/Fail 명확히
- **재현 가능** - 검증 방법 명시

## 출력 형식
```markdown
## Verification Report

### Spec: [spec 파일명]

### Success Criteria Check
| # | Criterion | Status | Evidence |
|---|-----------|--------|----------|
| 1 | 기준 1 | PASS/FAIL | 근거 |
| 2 | 기준 2 | PASS/FAIL | 근거 |

### E2E Test Results
- [ ] 시나리오 1: PASS/FAIL
- [ ] 시나리오 2: PASS/FAIL

### Verdict
- **PASS**: 모든 기준 충족
- **FAIL**: N개 기준 미충족
  - 미충족 항목 리스트

### Next Steps (if FAIL)
- 수정 필요 사항
```

## 사용 예시
```
/verify .claude/features/spring-boot-api/spec.md
```
