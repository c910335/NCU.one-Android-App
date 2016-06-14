# NCU.one-Android-App

Android App for [NCU.one](https://ncu.one).

# For developers

## Web

    <a href="ncunos://ncu.one?url=https%3A%2F%2Fgithub.com%2Fc910335%2FNCU.one-Android-App">Shorten https://github.com/c910335/NCU.one-Android-App</a>

## Android

    Intent intent = new Intent("tw.edu.ncu.nos.ncuone");
    intent.putExtra("url", "https://github.com/c910335/NCU.one-Android-App");
    startActivityForResult(intent, 1);

onActivityResult

    if (requestCode == 1 && resultCode == 1) {
        String shortUrl = data.getStringExtra("short_url");
        // do your work with shortUrl
    }

# Screenshot

![Screentshot](Screenshot.png)
