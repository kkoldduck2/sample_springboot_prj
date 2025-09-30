
#!/bin/bash

usage() {
  echo "Usage: $0 <concurrent_count> [delayMs] [randomDelay] [failRate] [apiCallCnt] [compositApiCallCnt] [randomCompositApiCall] [externalApiDelayMs]"
  echo "Example: $0 5 0 true 0 1 1 false 0"
  exit 1
}

# 필수 파라미터: 동시 건수
concurrent_count=$1
if [[ -z "$concurrent_count" ]]; then
  usage
fi

# 각 파라미터별 디폴트 값 설정 및 입력값이 있으면 덮어쓰기
delayMs=${2:-0}
randomDelay=${3:-true}
failRate=${4:-0}
apiCallCnt=${5:-1}
compositApiCallCnt=${6:-1}
randomCompositApiCall=${7:-false}
externalApiDelayMs=${8:-0}

# JSON payload 생성 (true/false는 소문자로 유지)
payload=$(cat <<EOF
{
  "delayMs": $delayMs,
  "randomDelay": $randomDelay,
  "failRate": $failRate,
  "apiCallCnt": $apiCallCnt,
  "compositApiCallCnt": $compositApiCallCnt,
  "randomCompositApiCall": $randomCompositApiCall,
  "externalApiDelayMs": $externalApiDelayMs
}
EOF
)

# 동시 요청 수행
for ((i=1; i<=concurrent_count; i++))
do
  sleep 0.1
  curl -X POST 'http://localhost:8080/scenario' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -d "$payload" &
done

wait

echo "Completed $concurrent_count concurrent requests."

