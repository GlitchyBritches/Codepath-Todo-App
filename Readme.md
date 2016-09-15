# Pre-work - ListFreak
**List Freak (v0.2)** for those who are total LIST FREAKS (i.e. a pretty cool Codepath Android Bootcamp Pre-work Project that makes lists & doesn't afraid of anything)**

##Summary of functionality
ListFreak allows users to add and remove to-do items. They can also add priorities, due dates, notes, and completion statuses to each item and customize the order of these items!

Submitted By: **Michael Turner**

Time spent: **30 hours** (spaced out over about a month - I was totally new to android dev)

## User Stories

The following **required** functionality is completed:

* [x] Add and remove todo items from the todo list
* [x] Persist todo items added
* [x] Tap a todo item, edit it's text on a separate screen, and have those changes appear on screen and persist to file


**Optional** functionality includes:
* [x] Persist/access items to a local sqlite database via a content provider
* [x] Optional item parameters priority, due date, note, and completion status can be associated with each item
* [x] New item creation, item parameter editing, and item date parameter editing is done with fragments
* [x] Theme is customized to use holo colors & design elements
* [x] App has a splashscreen which shows for only the time it takes app resources to load (and no shorter or longer)
* [x] Users can customize how objects are arranged (by priority, due date, or dated created) via the app bar
* [x] priority & due date views have headers to clarify due dates & priorities

## Video Walkthrough

Video walkthrough of implemented user stories:

<img src='https://thumbs.gfycat.com/GenerousKnobbyCuckoo-size_restricted.gif' title='Video Walkthrough' alt='Video Walkthrough' />

## Notes
Worked on it when I had time, started from zero knowledge of native android development (although not zero experience in mobile app development).  Attempted to do things correctly (used content providers, good fragment communication patterns, splashscreen the right way, holo design, appcompat usage, etc.).  Progress went very slowly at first, most time was spent upfront learning the android api & the many nuances, but things sped up a LOT as I learned the api.

## 3rd party library credits
* SectionCursorAdapter: The list adapter in this app which makes headers possible was created by extending SectionCursorAdapter ([github.com/twotoasters/SectionCursorAdapter](https://github.com/twotoasters/SectionCursorAdapter)) by twotoasters ([twotoasters.com](http://twotoasters.com))
* JodaTime: Used Joda Time to deal with dates: [joda.org/joda-time/](http://joda.org/joda-time/)
* ffmpeg: sample gif created using ffmpeg command line tool: [ffmpeg.org](https://ffmpeg.org/)

## License

    Copyright 2016 Michael Turner

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
