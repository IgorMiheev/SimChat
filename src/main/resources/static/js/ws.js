'use strict';
//активирован строгий режим

var messageForm = document.querySelector('#messageForm')
var messageInput = document.querySelector('#text');
var messageArea = document.querySelector('#messageArea')
var connectingElement = document.querySelector('.connecting')
var chatIdFromUrl = document.location.pathname.substr(document.location.pathname.lastIndexOf("message")+6) //получаем chat_id из url

var stompClient = null;
var username = null;

function onConnected(){
	stompClient.subscribe('/topic/public', onMessageRecieved);
	connectingElement.classList.add('hidden');
}

function onError(error){
	connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	connectingElement.style.color = 'red';
}


function send(event){
	if(!stompClient){
		usernameInPage = document.querySelector('#username').textContent.trim();
	
		if(usernameInPage){
			var socket = new SockJS('/chat-websocket');
			stompClient = Stomp.over(socket);
			
			stompClient.connect({}, onConnected,onError);
		}	
		event.preventDefault();
	} else {
		var messageContent = messageInput.value.trim();
		current_datetime = new Date();
		
		if (messageContent && stompClient){
			var MessageSended = {
				username: usernameInPage,
				content: messageInput.value,
				chat_id: chatIdFromUrl,
				creation_date: current_datetime.getFullYear()+"-"+("0"+(current_datetime.getMonth()+1)).slice(-2)+
				"-"+("0"+(current_datetime.getDate())).slice(-2)+
				" "+("0"+(current_datetime.getHours())).slice(-2)+
				":"+("0"+(current_datetime.getMinutes())).slice(-2)+
				":"+("0"+(current_datetime.getSeconds())).slice(-2),
				status: "NORMAL"
			};
			
			stompClient.send("/message/app/chat.send",{}, JSON.stringify(MessageSended));
			messageInput.value = '';
		}
		event.preventDefault();
	}
}

function onMessageRecieved(payload){
	var  messageCreate = JSON.parse(payload.body);
	
	if ( messageCreate.chat_id===roomIdFromUrl){
		var messageElement = document.createElement('li'); //элемент типа список
		
		messageElement.classList.add('chat-message');
		var usernameElement = document.createElement('span'); //элемент группировки
		var usernameText = document.createTextNode( messageCreate.username + ' (' +  messageCreate.creation_date + ')');
		usernameElement.appendChild(usernameText);
		messageElement.appendChild(usernameElement);
		
		var textElement  = document.createElement('p'); //элемент параграфа
		textextElement.innerHTML =  messageCreate.content;
		
		messageElement.appendChild(textElement);
		messageArea.appendChild(messageElement);
	}
}

messageForm.addEventListener('submit',send, true);
document.querySelector('#send').click();