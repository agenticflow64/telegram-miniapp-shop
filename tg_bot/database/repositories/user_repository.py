from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select
from database.models.user import User
from database.repositories.iuser_repository import IUserRepository

class UserRepository(IUserRepository):
    def __init__(self, session: AsyncSession):
        self.session = session

    async def get_user(self, telegram_id: int) -> Optional[User]:
        result = await self.session.execute(
            select(User).where(User.telegram_id == telegram_id)
        )
        return result.scalar_one_or_none()

    async def create_user(self, telegram_id: int, chat_id: int, username: str) -> Optional[User]:
        try:
            user = User(
                telegram_id=telegram_id,
                chat_id=chat_id,
                username=username
            )
            self.session.add(user)
            await self.session.commit()
            await self.session.refresh(user)
            return user
        except Exception:
            await self.session.rollback()
            raise