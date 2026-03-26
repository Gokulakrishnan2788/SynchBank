# Login — API Layer
# cat with: CONTEXT.md + base/api_contract.md + this file

## Generate these files ONLY
- LoginApiService.kt
- LoginRequestDto.kt
- LoginResponseDto.kt
- assets/mock/api/auth_login_success.json
- assets/mock/api/auth_login_error.json

## Endpoints
POST /auth/login
POST /auth/refresh
POST /auth/logout

## DTOs

### LoginRequestDto
{ "email": String, "password": String, "deviceId": String }

### LoginResponseDto
{ "userId": String, "token": String, "refreshToken": String,
  "expiresAt": Long, "userName": String, "accountType": String }

### RefreshRequestDto
{ "refreshToken": String }

### TokenResponseDto
{ "token": String, "refreshToken": String, "expiresAt": Long }

## Mock JSON — auth_login_success.json
{
  "success": true,
  "data": {
    "userId": "usr_001",
    "token": "mock_jwt_token_xyz",
    "refreshToken": "mock_refresh_token_xyz",
    "expiresAt": 9999999999000,
    "userName": "John Architect",
    "accountType": "INSTITUTIONAL"
  }
}

## Mock JSON — auth_login_error.json
{
  "success": false,
  "error": { "code": "AUTH_FAILED", "message": "Invalid credentials. Please try again." }
}

## Rules
- Do NOT create a new NetworkModule
- Do NOT create a new Retrofit instance
- Inject LoginApiService via Hilt in LoginModule.kt
