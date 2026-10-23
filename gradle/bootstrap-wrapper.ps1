$ErrorActionPreference = 'Stop'
$jar = Join-Path $PSScriptRoot 'wrapper\gradle-wrapper.jar'
$url = 'https://services.gradle.org/distributions/gradle-8.9-wrapper.jar'
$expected = '498495120a03b9a6ab5d155f5de3c8f0d986a449153702fb80fc80e134484f17'
try {
    Write-Host 'Downloading Gradle 8.9 wrapper from the official Gradle distribution server...'
    $tmp = "$jar.download"
    Invoke-WebRequest -Uri $url -OutFile $tmp -UseBasicParsing
    $actual = (Get-FileHash -Algorithm SHA256 -Path $tmp).Hash.ToLowerInvariant()
    if ($actual -ne $expected) { throw "SHA256 mismatch: $actual" }
    Move-Item -Force $tmp $jar
    Write-Host 'Gradle wrapper verified.'
} catch {
    Remove-Item -ErrorAction SilentlyContinue "$jar.download"
    Write-Error "Could not download verified wrapper: $_. Ensure you are online, then retry."
    exit 1
}
