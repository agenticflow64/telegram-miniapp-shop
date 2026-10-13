from typing import Optional
from database.repositories.user_repository import IUserRepository
from database.models.user import User
from services.iuser_service import IUserService

class UserService(IUserService):
    def __init__(self, user_repo):
        self.user_repo = user_repo

    async def register_user(self, telegram_id: int, chat_id: int, username: str) -> User:
        existing_user = await self.user_repo.get_user(telegram_id)
        if existing_user:
            return existing_user

        return await self.user_repo.create_user(
            telegram_id=telegram_id,
            chat_id=chat_id,
            username=username
        )