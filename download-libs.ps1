# Create directory if it doesn't exist
$libPath = "src/main/resources/static/js/lib"
New-Item -ItemType Directory -Force -Path $libPath

# Download jQuery
Invoke-WebRequest -Uri "https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js" -OutFile "$libPath/jquery.min.js"

# Download Bootstrap bundle
Invoke-WebRequest -Uri "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" -OutFile "$libPath/bootstrap.bundle.min.js"

# Download Moment.js
Invoke-WebRequest -Uri "https://cdn.jsdelivr.net/npm/moment@2.29.1/moment.min.js" -OutFile "$libPath/moment.min.js"

Write-Host "JavaScript libraries have been downloaded successfully!" 