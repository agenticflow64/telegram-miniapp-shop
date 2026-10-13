from contextlib import asynccontextmanager
from sqlalchemy.ext.asyncio import AsyncSession
from database.database import Database
from database.repositories.user_repository import UserRepository
from services.user_service import UserService

@asynccontextmanager
async def user_service_factory():
    session_factory = Database.get_session_factory()
    async with session_factory() as session:
        user_repo = UserRepository(session)
        yield UserService(user_repo)

@asynccontextmanager
async def db_session_factory():
    session_factory = Database.get_session_factory()
    async with session_factory() as session:
        yield session