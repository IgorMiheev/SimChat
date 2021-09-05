package com.simbirsoft.simchat.domain;

import java.io.Serializable;
import java.util.Set;

public class PartyID implements Serializable {

	private static final long serialVersionUID = 4975824955111899492L;
	private Set<Users> users;
	private Chat chat;

	public PartyID() {
	}

	public PartyID(Set<Users> users, Chat chat) {
		this.users = users;
		this.chat = chat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		result = prime * result + ((chat == null) ? 0 : chat.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartyID other = (PartyID) obj;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		if (chat == null) {
			if (other.chat != null)
				return false;
		} else if (!chat.equals(other.chat))
			return false;
		return true;
	}

}
