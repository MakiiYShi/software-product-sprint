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
 * Fetches data from the '/data' url and adds them to the DOM.
 */
function getData() {
  fetch('/data').then(response => response.json()).then((dataList) => {
    // dataList is an object, not a string, so we have to
    // reference its fields to create HTML content
    console.log("#### Get datalist, started by: " + dataList[0] + " ####");
    const dataListElement = document.getElementById('data-container');
    dataListElement.innerHTML = "";
    dataListElement.appendChild(
        createListElement("Data 0: " + dataList[0]));
    dataListElement.appendChild(
        createListElement("Data 1: " + dataList[1]));
    dataListElement.appendChild(
        createListElement("Data 2: " + dataList[2]));
  });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}