from abc import ABC, abstractmethod
from typing import Optional

from database.models.user import User

class IUserService(ABC):
    @abstractmethod
    async def register_user(self, telegram_id: int, chat_id: int, username: str) -> User:
        pass