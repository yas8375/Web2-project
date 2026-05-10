Param(
  [string]$OutDir = ".\\.certs",
  [string]$KeystoreFile = "localhost.p12",
  [string]$Alias = "movies-backend",
  [int]$Days = 365,
  [string]$StorePass
)

$ErrorActionPreference = "Stop"

if (-not $StorePass -or $StorePass.Trim().Length -lt 6) {
  Write-Host "Please provide -StorePass with at least 6 characters." -ForegroundColor Yellow
  Write-Host "Example: .\\scripts\\generate-dev-ssl.ps1 -StorePass \"change-me-123\""
  exit 1
}

New-Item -ItemType Directory -Force -Path $OutDir | Out-Null
$outPath = Join-Path $OutDir $KeystoreFile

Write-Host "Generating self-signed dev certificate at: $outPath"
Write-Host "Alias: $Alias, Days: $Days"

# Requires JDK (keytool) in PATH.
keytool -genkeypair `
  -alias $Alias `
  -keyalg RSA `
  -keysize 2048 `
  -storetype PKCS12 `
  -keystore $outPath `
  -validity $Days `
  -storepass $StorePass `
  -keypass $StorePass `
  -dname "CN=localhost, OU=Dev, O=MovieEnjoy, L=Riyadh, S=Riyadh, C=SA" `
  -ext "SAN=dns:localhost,ip:127.0.0.1"

Write-Host ""
Write-Host "Next: run backend with HTTPS profile:"
Write-Host "  `$env:SPRING_PROFILES_ACTIVE='https'"
Write-Host "  `$env:SSL_KEYSTORE_PATH='$(Resolve-Path $outPath)'"
Write-Host "  `$env:SSL_KEYSTORE_PASSWORD='$StorePass'"
Write-Host "  .\\mvnw.cmd spring-boot:run"

