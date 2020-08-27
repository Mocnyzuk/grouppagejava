'use strict';
document.querySelector('#welcomeForm').addEventListener('submit', connect, true)
document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true)
var stompClient = null;
var name = null;
function connect(event) {
    name = document.querySelector('#name').value.trim();
    if (name) {
        document.querySelector('#welcome-page').classList.add('hidden');
        document.querySelector('#dialogue-page').classList.remove('hidden');
        var socket = new SockJS('/websocketApp');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, connectionSuccess);
    }
    event.preventDefault();
}
function connectionSuccess() {
    stompClient.subscribe('/topic/1', onMessageReceived);
    // stompClient.send("/app/conversation/22/sendmessage", {}, JSON.stringify({
    //     participantId : name,
    //     type : 'newUser'
    // }))
}
function sendMessage(event) {
    var messageContent = document.querySelector('#chatMessage').value.trim();
    if (messageContent && stompClient) {
        var chatMessage = {
            participantId : 59,
            content : document.querySelector('#chatMessage').value,
            type : 'CHAT'
        };
        stompClient.send("/app/conversation/22/sendmessage", {},
            JSON.stringify(chatMessage));
        document.querySelector('#chatMessage').value = '';
    }
    event.preventDefault();
}
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');
    messageElement.classList.add('message-data');
    var element = document.createElement('i');
    var text = document.createTextNode(message.participantId[0]);
    element.appendChild(text);
    messageElement.appendChild(element);
    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(message.participantId);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);
    document.querySelector('#messageList').appendChild(messageElement);
    document.querySelector('#messageList').scrollTop = document
        .querySelector('#messageList').scrollHeight;
}