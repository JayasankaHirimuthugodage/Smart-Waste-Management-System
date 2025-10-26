# Run tests and display coverage report
# This script runs Maven tests and then displays the coverage in a formatted table

Write-Host ""
Write-Host "Running tests with coverage..." -ForegroundColor Cyan
Write-Host ""

# Run Maven tests
mvn clean test

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "Tests completed successfully! Generating coverage report..." -ForegroundColor Green
    Write-Host ""
    
    # Display coverage
    & ".\show-coverage.ps1"
} else {
    Write-Host ""
    Write-Host "Tests failed! Please fix the errors and try again." -ForegroundColor Red
    Write-Host ""
    exit 1
}
