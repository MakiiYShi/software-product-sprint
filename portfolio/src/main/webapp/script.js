// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['I was born and raised in Jiangsu province.',
      'It was my first time to visit Bei Jing when registering at Tsinghua.', 
      'My very first line of code was written in summer, 2014.',
      'Japanese food is quite to my appetite', 
      'I am a great fan of musical and dance drama.', 
      'Join me in dancing at any time!'];
  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];
  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/**
 * Fetches login status from the '/login' url and respond to it.
 */
function getLogin() {
  fetch('/login').then(response => response.json()).then((dataMap) => {
    // dataMap: isLogged(True or False), Email(if isLogged), Url(if not Logged).

    const formElement = document.getElementById('form-part');
    const messageElement = document.getElementById('message-part');
    if (dataMap['IsLogged'] == 'False') {
        formElement.style = 'display:none';
        messageElement.innerHTML = "<p>Hi stranger, please login <a href=\"" + dataMap["Url"] + "\">here</a> to comment.</p>";
    } else {
        messageElement.innerHTML = "<p>Hello " + dataMap["Email"] + ", welcome!</p> <p>Have something to comment?</p>";
    }

  });
}

/**
 * Fetches comment data from the '/data' url and add to the portfolio.
 */
function getData() {
  fetch('/data').then(response => response.json()).then((dataList) => {
    // dataList: email + comment.

    const dataElement = document.getElementById('data-container');
    dataElement.innerHTML = "";
    for (var i = 0; i < dataList.length; i++) {
        dataElement.append(createListElement(dataList[i]));
    }

  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}