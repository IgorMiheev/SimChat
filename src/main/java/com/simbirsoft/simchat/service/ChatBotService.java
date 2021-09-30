package com.simbirsoft.simchat.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.simbirsoft.simchat.domain.ChatEntity;
import com.simbirsoft.simchat.domain.PartyEntity;
import com.simbirsoft.simchat.domain.UsrEntity;
import com.simbirsoft.simchat.domain.dto.ChatBotCommand;
import com.simbirsoft.simchat.domain.dto.ChatCreate;
import com.simbirsoft.simchat.domain.dto.PartyCreate;
import com.simbirsoft.simchat.exception.ChatAlreadyExistException;
import com.simbirsoft.simchat.exception.ChatNotFoundException;
import com.simbirsoft.simchat.exception.PartyAlreadyExistException;
import com.simbirsoft.simchat.exception.PartyNotFoundException;
import com.simbirsoft.simchat.exception.UsrNotFoundException;
import com.simbirsoft.simchat.repository.ChatRepository;
import com.simbirsoft.simchat.repository.PartyRepository;
import com.simbirsoft.simchat.repository.UsrRepository;

@Service
public class ChatBotService {

	@Autowired
	private UsrService usrService;

	@Autowired
	private ChatService chatService;

	@Autowired
	private AccessService accessService;

	@Autowired
	private PartyService partyService;

	@Autowired
	private UsrRepository usrRepository;

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private PartyRepository partyRepository;

	Long currentUserId = 1L;

	enum BaseCommand {
		ROOM_CREATE("//room create"), ROOM_REMOVE("//room remove"), ROOM_RENAME("//room rename"),
		ROOM_CONNECT("//room connect"), ROOM_DISCONNECT("//room disconnect"), USER_RENAME("//user rename"),
		USER_BAN("//user ban"), USER_MODERATOR("//user moderator"), YBOT_FIND("//yBot find"), YBOT_HELP("//yBot help"),
		HELP("//help");

		private String text;

		private BaseCommand(String name) {
			this.text = name;
		}

		public String getText() {
			return text;
		}

		public static BaseCommand getEnumByText(String text) {
			for (BaseCommand bc : BaseCommand.values()) {
				if (bc.getText().equals(text)) {
					return bc;
				}
			}
			return null;
		}
	}

	public ResponseEntity parseBotCommand(ChatBotCommand chatBotCommand) throws UsrNotFoundException,
			ChatAlreadyExistException, ChatNotFoundException, PartyAlreadyExistException, PartyNotFoundException {

		String cmd = chatBotCommand.getText();
		String[] splitBase = cmd.split(" ");
		String baseText = splitBase[0];
		BaseCommand baseCommand = BaseCommand.getEnumByText(baseText);
		String additionCommand = "";
		if (baseCommand == null) {
			baseText = splitBase[0] + " " + splitBase[1];
			baseCommand = BaseCommand.getEnumByText(baseText);
		}
		if (baseCommand == null) {
			return ResponseEntity.badRequest()
					.body("Такой команды не существует. Введите //help для получения списка комманд");
		}
		if (cmd.split(baseText).length > 1) {
			additionCommand = cmd.split(baseText)[1];
		}
		switch (baseCommand) {
		case ROOM_CREATE:
			return parseCmdAndCreateRoom(additionCommand);
		case ROOM_CONNECT:
			return parseCmdAndConnectToRoom(additionCommand);
		case ROOM_DISCONNECT:
			return parseCmdAndDisconnectFromRoom(additionCommand);
		case ROOM_REMOVE:
			return ResponseEntity.ok("Введена команда " + BaseCommand.ROOM_REMOVE.getText() + additionCommand);
		// break;
		case ROOM_RENAME:
			return ResponseEntity.ok("Введена команда " + BaseCommand.ROOM_RENAME.getText() + additionCommand);
		// break;
		case USER_BAN:
			return ResponseEntity.ok("Введена команда " + BaseCommand.USER_BAN.getText() + additionCommand);
		// break;
		case USER_MODERATOR:
			return ResponseEntity.ok("Введена команда " + BaseCommand.USER_MODERATOR.getText() + additionCommand);
		// break;
		case USER_RENAME:
			return ResponseEntity.ok("Введена команда " + BaseCommand.USER_RENAME.getText() + additionCommand);
		// break;
		case YBOT_FIND:
			return ResponseEntity.ok("Введена команда " + BaseCommand.YBOT_FIND.getText() + additionCommand);
		// break;
		case YBOT_HELP:
			return ResponseEntity.ok("Введена команда " + BaseCommand.YBOT_HELP.getText() + additionCommand);
		// break;
		case HELP:
			return ResponseEntity.ok("Введена команда " + BaseCommand.HELP.getText() + additionCommand);
		// break;

		default:
			break;
		}

		return ResponseEntity.ok(null);
	}

	/**
	 * @param additionCommand
	 * @return
	 * @throws UsrNotFoundException
	 * @throws ChatAlreadyExistException
	 */
	public ResponseEntity parseCmdAndCreateRoom(String additionCommand)
			throws UsrNotFoundException, ChatAlreadyExistException {
		additionCommand = additionCommand.trim();
		String roomName = "";
		String chatType = "";
		try {
			// получаем имя чата между первым символом { и последним }
			roomName = additionCommand.substring(additionCommand.indexOf("{") + 1, additionCommand.lastIndexOf("}"));

		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд");
		}

		// проверка на то, если ли что-нибудь после символа названия чата.
		if (additionCommand.lastIndexOf("}") < additionCommand.trim().length() - 1) {

			if (additionCommand.substring(additionCommand.lastIndexOf("}") + 1).equals(" -c")) {
				chatType = "private";
			} else {
				return ResponseEntity.badRequest()
						.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд.");
			}
		} else {
			chatType = "public";
		}
		ChatCreate modelCreate = new ChatCreate(roomName, currentUserId, chatType);
		chatService.create(modelCreate);
		return ResponseEntity.ok("Введена команда " + BaseCommand.ROOM_CREATE.getText() + " " + additionCommand
				+ ". Команда выполнена успешно.");
	}

	public ResponseEntity parseCmdAndConnectToRoom(String additionCommand)
			throws UsrNotFoundException, ChatNotFoundException, PartyAlreadyExistException {
		additionCommand = additionCommand.trim();
		String roomName = "";
		String userName = "";
		try {
			// получаем имя чата между первым символом { и первым }
			roomName = additionCommand.substring(additionCommand.indexOf("{") + 1, additionCommand.indexOf("}"));
			// получаем имя пользователя между последним символом { и последним }
			if (additionCommand.contains("-l {")) {
				userName = additionCommand.substring(additionCommand.lastIndexOf("{") + 1,
						additionCommand.lastIndexOf("}"));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд");
		}

		// проверка на то, если ли что-нибудь после символа названия чата.
		if (additionCommand.lastIndexOf("}") < additionCommand.trim().length() - 1) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд.");

		}

		ChatEntity chatEntity = null;

		if (!roomName.equals("")) {
			chatEntity = chatRepository.findByName(roomName);
		}

		UsrEntity userEntity = null;

		if (additionCommand.contains("-l {")) {
			userEntity = usrRepository.findByUsername(userName);
		} else {
			userEntity = usrRepository.findById(currentUserId).orElse(null);
		}

		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким именем не найден");
		}
		if (userEntity == null) {
			throw new UsrNotFoundException("Пользователь с именем " + userName + " не найден");

		}

		PartyCreate modelCreate = new PartyCreate(chatEntity.getChat_id(), userEntity.getUser_id(), "member",
				java.sql.Timestamp.valueOf(LocalDateTime.now()));
		partyService.create(modelCreate);

		return ResponseEntity.ok("Введена команда " + BaseCommand.ROOM_CREATE.getText() + " " + additionCommand
				+ ". Команда выполнена успешно.");
	}

	public ResponseEntity parseCmdAndDisconnectFromRoom(String additionCommand)
			throws UsrNotFoundException, ChatNotFoundException, PartyAlreadyExistException, PartyNotFoundException {
		additionCommand = additionCommand.trim();
		String roomName = null;
		String userName = null;
		Long banTime = null;
		try {
			// получаем имя чата между первым символом { и первым }
			roomName = additionCommand.substring(additionCommand.indexOf("{") + 1, additionCommand.indexOf("}"));

			// получаем имя пользователя
			if (additionCommand.contains("-l {")) {
				String sub = additionCommand.substring(additionCommand.indexOf("-l {") + 4);
				userName = sub.substring(0, sub.indexOf("}"));

				// получаем время бана в минутах
				if (additionCommand.contains("-m {")) {
					String subs = additionCommand.substring(additionCommand.indexOf("-m {") + 4);
					banTime = Long.parseLong(subs.substring(0, subs.indexOf("}")));
				}
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд");
		}

		// проверка на то, если ли что-нибудь после символа названия чата.
		if (additionCommand.lastIndexOf("}") < additionCommand.trim().length() - 1) {
			return ResponseEntity.badRequest()
					.body("Ошибка в синтаксисе команды. Введите //help для получения списка комманд.");
		}
		if (!additionCommand.contains("-l {") && additionCommand.contains("-m {")) {
			return ResponseEntity.badRequest().body(
					"Ошибка в синтаксисе команды. Нельзя использовать параметр -m без параметра -l. Введите //help для получения списка комманд");
		}

		ChatEntity chatEntity = null;

		if (!roomName.equals("")) {
			chatEntity = chatRepository.findByName(roomName);
		}

		if (chatEntity == null) {
			throw new ChatNotFoundException("Чат с таким именем не найден");
		}

		if (additionCommand.contains("-l {")) {
			UsrEntity userEntity = null;

			userEntity = usrRepository.findByUsername(userName);

			if (userEntity == null) {
				throw new UsrNotFoundException("Пользователь с именем " + userName + " не найден");
			}

			PartyEntity partyEntity = partyRepository.findByUserAndChat(userEntity, chatEntity);

			if (partyEntity == null) {

				if (banTime != null) {

					PartyCreate modelCreate = new PartyCreate(chatEntity.getChat_id(), userEntity.getUser_id(),
							"banned_member", java.sql.Timestamp.valueOf(LocalDateTime.now()));
					partyService.create(modelCreate);
					partyEntity = partyRepository.findByUserAndChat(userEntity, chatEntity);

				} else {

					throw new PartyNotFoundException("Пользователь " + userEntity.getUsername() + " в чате "
							+ chatEntity.getName() + " не состоит");
				}
			}

			if (banTime != null) {

				partyService.banUserByIdAndChatId(userEntity.getUser_id(), chatEntity.getChat_id(), banTime);

				return ResponseEntity.ok("Готово. Пользователь " + userEntity.getUsername() + " с id="
						+ userEntity.getUser_id() + " не сможет войти в этот чат " + banTime + " минут");
			} else {

				partyService.delete(partyEntity.getParty_id());

				return ResponseEntity.ok("Готово. Пользователь " + userEntity.getUsername() + " с id="
						+ userEntity.getUser_id() + " отключен от чата " + chatEntity.getName());
			}
		} else {
			// выход текущего пользователя из комнаты = room disconnect

			UsrEntity userEntity = usrRepository.findById(currentUserId).orElse(null);

			PartyEntity partyEntity = partyRepository.findByUserAndChat(userEntity, chatEntity);

			if (partyEntity == null) {
				throw new PartyNotFoundException(
						"Пользователь " + userEntity.getUsername() + " в чате " + chatEntity.getName() + " не состоит");
			} else {
				partyService.delete(partyEntity.getParty_id());

				return ResponseEntity.ok("Готово. Пользователь " + userEntity.getUsername() + " с id="
						+ userEntity.getUser_id() + " отключен от чата " + chatEntity.getName());
			}

		}

	}

}
