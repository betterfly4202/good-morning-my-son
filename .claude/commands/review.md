# /review - 코드 리뷰

변경된 코드를 리뷰하고 이슈를 식별합니다.

## 입력
- 리뷰 대상 파일 또는 git diff

## 프로세스

```
┌─────────────────────────────────────────┐
│  1. 코드 분석                           │
│  2. 이슈 식별 ←─────────────┐           │
│       ↓                     │           │
│  High 이슈? ─── Yes ─→ Fix ─┘ (최대 2회) │
│       ↓                                 │
│      No                                 │
│       ↓                                 │
│  3. 리뷰 완료                           │
└─────────────────────────────────────────┘
```

### 상세 단계
1. **코드 분석** - 변경사항 전체 파악
2. **이슈 식별** - 평가 기준에 따라 분류
3. **Fix 루프** (High 이슈 있을 경우, 최대 2회):
   - High 이슈 수정
   - 재리뷰
   - 2회 후에도 High 있으면 → 멈추고 보고

## 코드 원칙
`.claude/docs/code-constitution.md` 준수

리뷰 시 체크:
- [ ] 최소 단위로 구현되었는가? (Rule 5)
- [ ] 상태를 최소화했는가? (Rule 11)
- [ ] 불변 데이터, 파이프라인 설계인가? (Rule 12)
- [ ] 단일 책임인가? (Rule 14)
- [ ] 로깅/관찰 가능한가? (Rule 17)
- [ ] FORBIDDEN DESIGNS 위반 없는가? (Rule 19)

## 평가 기준
1. **요구사항 일치** - spec과 맞는지
2. **정확성** - 버그, 엣지 케이스
3. **유지보수성** - 가독성, 복잡도
4. **테스트 커버리지** - 누락된 테스트
5. **Constitution 준수** - 위 코드 원칙 체크리스트

## 규칙
- **코드 수정 금지** - 이슈만 보고 (Fix 단계에서 수정)
- **심각도 명시** - High/Medium/Low
- **수정 제안** - 구체적으로
- **2회 제한** - 무한 루프 방지

## 출력 형식
```markdown
## Review Summary
- Files reviewed: N
- Issues found: N (High: N, Medium: N, Low: N)
- Fix iteration: 1/2

## Constitution Compliance
| Rule | Check | Status |
|------|-------|--------|
| 5 | 최소 단위 | ✅/❌ |
| 11 | 상태 최소화 | ✅/❌ |
| 12 | 불변/파이프라인 | ✅/❌ |
| 14 | 단일 책임 | ✅/❌ |
| 17 | 관찰 가능성 | ✅/❌ |
| 19 | FORBIDDEN | ✅/❌ |

## Blocking Issues (High)
- [ ] 파일:라인 - 이슈 설명
  - 수정 제안: ...

## Non-blocking Issues (Medium/Low)
- [ ] 파일:라인 - 이슈 설명

## Missing Tests
- [ ] 필요한 테스트 케이스

## Spec Mismatches
- [ ] spec과 다른 부분

## Verdict
- [ ] PASS - High 이슈 없음, Constitution 준수
- [ ] PASS with notes - Medium/Low만 존재
- [ ] FAIL - High 이슈 존재 → Fix 필요
```

## 2회 실패 시 보고 형식
```markdown
## Review Failed - High Issues Remain

### Fix 시도: 2/2

### 해결되지 않은 High 이슈
1. {이슈 설명}
2. {이슈 설명}

### 도움 필요
- {구체적인 질문 또는 요청}
```

## 사용 예시
```
/review src/main/kotlin/com/goodmorning/subtitle/
/review (현재 staged 변경사항 리뷰)
```
