## RSA KEY 생성 방법
### Local Test 시 꼭 생성 해주세요. 

```bash
$ cd auth-server/src/main/resources/keys
$ openssl genrsa -out key-pair.pem 2048
$ openssl rsa -in key-pair.pem -out public-key.pem -pubout
$ openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in key-pair.pem -out private-key.pem
$ rm key-pair.pem
```