## Searche Engine App
### About App
This Application makes a text searching on entered website. With help of BFS algorithm the app scan each found unique urls and searching text until scanned urls count will be equals to entered number.
User can write searching word, initial website, count of threads and max pages.
The result is produced as list where each item is url and count of coincidence or error message.
This app searching urls is urls, which start with "https://" and end with any char. which can`t be in url.

Language - Kotlin.
Architecture - MVVM.
Technologies:
Android app components,
RxJava2,
Retrofit2,
DataBinding.

### About Tests
For testing this app I propose to mock url which has knowable count of urls and knowable count of searching text.    

<img src="https://user-images.githubusercontent.com/40399341/122100392-cb965d00-ce1b-11eb-819f-73b6a2989352.png" data-canonical-src="https://user-images.githubusercontent.com/40399341/122100392-cb965d00-ce1b-11eb-819f-73b6a2989352.png" width="200" height="400" />
<img src="https://user-images.githubusercontent.com/40399341/122100394-ccc78a00-ce1b-11eb-9cf3-675903d35ea6.png" data-canonical-src="https://user-images.githubusercontent.com/40399341/122100394-ccc78a00-ce1b-11eb-9cf3-675903d35ea6.png" width="200" height="400" />
