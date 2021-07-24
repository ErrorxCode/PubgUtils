
# PUBGM Cheats
<p align="left">
  <a href="#"><img alt="Languages-Java" src="https://img.shields.io/badge/Language-Java-1DA1F2?style=flat-square&logo=java"></a>
  <a href="#"><img alt="Version" src="https://img.shields.io/badge/Library version-1.0-blue"></a>
  <a href="#"><img alt="Bot" src="https://img.shields.io/badge/PUBG version-1.4.0-orange"></a>
  <a href="https://www.instagram.com/x__coder__x/"><img alt="Instagram - x__coder__" src="https://img.shields.io/badge/Instagram-x____coder____x-lightgrey"></a>
  <a href="#"><img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/ErrorxCode/OTP-Verification-Api?style=social"></a>
  </p>
ᴛʜɪs ɪs ᴀ ᴀɴᴅʀᴏɪᴅ ʟɪʙʀᴀʀʏ (.ᴀʀʀ) ғᴏʀ ᴍᴀᴋɪɴɢ ᴘᴜʙɢ ᴄʜᴇᴀᴛs (ʜᴀᴄᴋs) ɪɴ ᴍᴏʀᴇ ᴘʀᴏғᴇssɪᴏɴᴀʟ & ᴇᴀsʏ ᴡᴀʏ. ᴛʜɪs ʟɪʙʀᴀʀʏ ᴄᴏɴᴛᴀɪɴs ʙᴀsɪᴄ ғɪʟᴇs ᴏᴘᴇʀᴀᴛɪᴏɴs ᴡʜɪᴄʜ ɪs ᴜsᴇᴅ ɪɴ ᴍᴀᴋɪɴɢ ᴄʜᴇᴀᴛs. ᴛʜɪs ʟɪʙʀᴀʀʏ ᴏɴʟʏ ᴍᴀᴋᴇs ʏᴏᴜʀ ᴡᴏʀᴋ ᴇᴀsʏ, ɪᴛ ᴅᴏ ɴᴏᴛ ᴘʀᴏᴠɪᴅᴇ ᴀɴʏ ᴛʏᴘᴇ ᴏғ ᴍᴏᴅ ғɪʟᴇs ᴏʀ ᴀɴʏ ᴋɪɴᴅ ᴏғ ᴘᴜʙɢ ᴄʜᴇᴀᴛs.

## Usage / Examples
To clear caches :-
```java
PubgUtils utils = new PubgUtils(this,PubgUtils.GLOBAL);
utils.clearCache(); 

// or utils.clearData() ;to delete all the data of the game
```
To run a cpp :-
```java
utils.runCPP(getExternalFileDir(null) + "/aimbot.cpp");

// or utils.runShell("su"); to execute a shell command
```

## Implimentation
Add maven to your root build.gradle (Project level)
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency (Module level)
```
dependencies {
	    implementation 'com.github.ErrorxCode:PubgUtils:1.0'
}
```

## Documentation

[Javadocs](https://wwhzfp8wqlzivba0csou4g-on.drv.tw/www.docs.pubgutils/com/pubg/utils/package-summary.html)


  
## FAQ

#### Q. Can we make ESP using this library ?

A. No, this library is only for files modifications. You can build Mod data tool but not ESP.

#### Q. Is playing with cheats illigal ?

A. Yes, you will be banned if caught for cheating.

#### Q. Are the cheats safe made using this library ?

A. Not a valid question. This totally depends on the type of cheat & the files or code you used. Again, this library do not provide any type of cheat or mod files. this only make your work easy nothing else.

  
  
