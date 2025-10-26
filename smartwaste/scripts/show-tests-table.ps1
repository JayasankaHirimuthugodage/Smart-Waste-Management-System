<#
Runs Maven tests (optional) and prints a table of test results parsed from Surefire XML reports.

Usage:
  # Run tests and show table
  .\show-tests-table.ps1 -RunTests

  # Just parse existing reports and show table
  .\show-tests-table.ps1
#>

param(
    [switch]$RunTests,
    [string[]]$Classes
)

Set-Location -Path (Split-Path -Parent $MyInvocation.MyCommand.Definition)
Set-Location -Path ..\

if ($RunTests) {
    Write-Host "Running 'mvn test'..." -ForegroundColor Cyan
    mvn test
}

$reportsDir = Join-Path -Path $PWD -ChildPath 'target\surefire-reports'
if (-not (Test-Path $reportsDir)) {
    Write-Host "No surefire reports found in $reportsDir" -ForegroundColor Yellow
    exit 1
}

$xmlFiles = Get-ChildItem -Path $reportsDir -Filter 'TEST-*.xml' -File -ErrorAction SilentlyContinue
if (-not $xmlFiles -or $xmlFiles.Count -eq 0) {
    Write-Host "No TEST-*.xml files found in $reportsDir" -ForegroundColor Yellow
    exit 1
}

$results = @()
foreach ($file in $xmlFiles) {
    try {
        [xml]$doc = Get-Content $file.FullName -Raw
        $ts = $doc.testsuite
        if (-not $ts) { continue }

        $name = $ts.name
        $tests = [int]$ts.tests
        $failures = [int]$ts.failures
        $errors = [int]$ts.errors
        $skipped = [int]$ts.skipped
        $time = [double]$ts.time

        $status = if (($failures + $errors) -gt 0) { 'FAILED' } else { 'PASSED' }

        $results += [PSCustomObject]@{
            Class        = $name
            Tests        = $tests
            Failures     = $failures
            Errors       = $errors
            Skipped      = $skipped
            Time_s       = '{0:N3}' -f $time
            Status       = $status
        }
    }
    catch {
        Write-Warning "Failed to parse $($file.FullName): $_"
    }
}


# By default, show only the three selected test classes if no -Classes provided
if (-not $Classes -or $Classes.Count -eq 0) {
    $Classes = @(
        'com.csse.smartwaste.payment.service.StripePaymentServiceTest',
        'com.csse.smartwaste.pickup.service.PickupRequestServiceTest',
        'com.csse.smartwaste.resident.controller.ResidentControllerTest'
    )
}

$filtered = $results | Where-Object { $Classes -contains $_.Class }

if (-not $filtered -or $filtered.Count -eq 0) {
    Write-Host "No matching test classes found for the requested filters." -ForegroundColor Yellow
    Write-Host "Requested classes:" -NoNewline; Write-Host " $Classes" -ForegroundColor Cyan
    exit 1
}

function Show-TestTable {
    param(
        [array]$Rows,
        [string[]]$Columns
    )

    # compute widths
    $widths = @{}
    # compute max class width based on console width to avoid wrapping
    try { $consoleWidth = $Host.UI.RawUI.BufferSize.Width } catch { $consoleWidth = 120 }
    $maxClassWidth = [Math]::Max(30, $consoleWidth - 60)
    foreach ($col in $Columns) {
        $max = ($col.Length)
        foreach ($r in $Rows) {
            $val = [string]($r.$col)
            if ($col -eq 'Class' -and $val.Length -gt $maxClassWidth) {
                $vlen = $maxClassWidth
            } else {
                $vlen = $val.Length
            }
            if ($vlen -gt $max) { $max = $vlen }
        }
        $widths[$col] = $max
    }

    # helpers (use char codes to avoid encoding issues)
    $h = ([string][char]0x2500); $v = ([string][char]0x2502); $tl=([string][char]0x250C); $tr=([string][char]0x2510); $bl=([string][char]0x2514); $br=([string][char]0x2518); $tm=([string][char]0x252C); $bm=([string][char]0x2534); $lm=([string][char]0x251C); $rm=([string][char]0x2524); $mm=([string][char]0x253C)

    # top border
    $top = $tl
    foreach ($col in $Columns) {
        $top += $h * ($widths[$col] + 2)
        if ($col -ne $Columns[-1]) { $top += $tm } else { $top += $tr }
    }
    Write-Host $top -ForegroundColor DarkCyan

    # header
    foreach ($col in $Columns) {
        $cell = ' ' + $col.PadRight($widths[$col]) + ' '
        Write-Host -NoNewline $cell -ForegroundColor Cyan
        if ($col -ne $Columns[-1]) { Write-Host -NoNewline $v -ForegroundColor DarkCyan }
    }
    Write-Host $v

    # separator
    $sep = $lm
    foreach ($col in $Columns) {
        $sep += $h * ($widths[$col] + 2)
        if ($col -ne $Columns[-1]) { $sep += $mm } else { $sep += $rm }
    }
    Write-Host $sep -ForegroundColor DarkCyan

    # rows
    foreach ($r in $Rows) {
        
        foreach ($col in $Columns) {
            $val = [string]($r.$col)
            if ($col -eq 'Class' -and $val.Length -gt $maxClassWidth) {
                $display = $val.Substring(0, $maxClassWidth - 3) + '...'
            } else { $display = $val }
            $cell = ' ' + $display.PadRight($widths[$col]) + ' '
            if ($col -eq 'Status') {
                if ($val -eq 'PASSED') { Write-Host -NoNewline $cell -ForegroundColor Green } 
                elseif ($val -eq 'FAILED') { Write-Host -NoNewline $cell -ForegroundColor Red } 
                else { Write-Host -NoNewline $cell -ForegroundColor Yellow }
            } else {
                Write-Host -NoNewline $cell -ForegroundColor White
            }
            if ($col -ne $Columns[-1]) { Write-Host -NoNewline $v }
        }
        Write-Host $v
    }

    # bottom border
    $bot = $bl
    foreach ($col in $Columns) {
        $bot += $h * ($widths[$col] + 2)
        if ($col -ne $Columns[-1]) { $bot += $bm } else { $bot += $br }
    }
    Write-Host $bot -ForegroundColor DarkCyan
}

$columns = @('Class','Tests','Failures','Errors','Skipped','Time_s','Status')
$rows = $filtered | Sort-Object -Property Status, Class | ForEach-Object { $_ }

Show-TestTable -Rows $rows -Columns $columns


$totals = [PSCustomObject]@{
    TotalSuites = $filtered.Count
    TotalTests   = ($filtered | Measure-Object Tests -Sum).Sum
    TotalFailed  = (($filtered | Measure-Object Failures -Sum).Sum) + (($filtered | Measure-Object Errors -Sum).Sum)
    TotalSkipped = ($filtered | Measure-Object Skipped -Sum).Sum
    TotalTime_s  = '{0:N3}' -f (($filtered | ForEach-Object {[double]$_.Time_s}) | Measure-Object -Sum).Sum
}

Write-Host "`nSummary:" -ForegroundColor Cyan
$totals | Format-List
