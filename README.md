# Music Player
A beautiful looking music player for android which has been implemented 
using Kotlin as a programming language.

## Screen Shots
<img src="https://github.com/melikaafrakhteh/Music-player/blob/main/screenShots/cover.png" width="200px"/></a>
<img src="https://github.com/melikaafrakhteh/Music-player/blob/main/screenShots/allmusic.png" width="200px"/></a>
<img src="https://github.com/melikaafrakhteh/Music-player/blob/main/screenShots/dialog.png" width="200px"/></a>
<img src="https://github.com/melikaafrakhteh/Music-player/blob/main/screenShots/player.png" width="200px"/></a>

## Technologies and Methodologies which used:
 - **ExoPlayer 2**: Plays audio files with custom playback controls.
 - **Dagger 2**: For Dependency injection (builds and provides dependencies across the application).
 - **Kotlin Coroutines**: handles tasks in the background to improve the application's performance.
 - **Room**: saves data (saves favorite music and playlists).
 - **LiveData**: delivers updates to the observers only when data changes.
 - **ViewPager 2**: provides the functionality to flip pages (fragments) in the application.
 - **RxJava, RxAndroid, RxKotlin**: handles heavy operations
    (reading the input audio file by Media Extractor,
    then decoding the encoded input audio by Media Codec) and returns the results to the main thread.

For architecture, I use Clean architecture with MVVM, MVI, MVP, and repository pattern.

## The features of this application:
 - attractive Ui
 - Users can create playlists and add or remove music from them.
 - Users can add or remove music from liked list.
 - Users can see which music has been added to their phones in the last week.

## Supported android version:
 Android 6.0 Marshmallow (Api level 23) or higher.
