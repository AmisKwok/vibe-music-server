<#
.SYNOPSIS
从模板创建新的配置环境

.DESCRIPTION
此脚本将config-template中的模板文件复制到指定环境文件夹，并移除.template后缀

.PARAMETER Environment
目标环境名称 (如: dev, local, preview, prod, staging等)

.EXAMPLE
.\create-config-from-template.ps1 -Environment "staging"
#>

param(
    [Parameter(Mandatory=$true)]
    [string]$Environment
)

# 检查目标文件夹是否存在
$targetDir = "config/$Environment"
if (Test-Path $targetDir) {
    Write-Host "目标文件夹 $targetDir 已存在，请选择其他环境名称或删除现有文件夹" -ForegroundColor Red
    exit 1
}

# 创建目标文件夹
New-Item -ItemType Directory -Path $targetDir -Force | Out-Null

# 复制所有模板文件
$templateFiles = Get-ChildItem -Path "config-template/dev" -Filter "*.template"
foreach ($file in $templateFiles) {
    $targetFile = Join-Path $targetDir $file.Name.Replace('.template', '')
    Copy-Item $file.FullName -Destination $targetFile
    Write-Host "已创建: $targetFile" -ForegroundColor Green
}

Write-Host "`n配置环境 '$Environment' 创建完成！" -ForegroundColor Green
Write-Host "请编辑 $targetDir 中的配置文件并填写实际参数" -ForegroundColor Yellow
Write-Host "注意: 配置文件已从Git跟踪中排除，不会提交到版本控制" -ForegroundColor Cyan