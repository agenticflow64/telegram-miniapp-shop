from abc import ABC, abstractmethod
from typing import Optional
from database.models.user import User

class IUserRepository(ABC):
    @abstractmethod
    async def get_user(self, telegram_id: int) -> Optional[User]:
        pass

    @abstractmethod
    async def create_user(self, telegram_id: int, chat_id: int, username: str) -> Optional[User]:
        pass