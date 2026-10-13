from datetime import datetime
from sqlalchemy.orm import mapped_column, Mapped
from sqlalchemy import text, String, BigInteger
from database.database import Base

class User(Base):
    __tablename__ = '_user'
    id: Mapped[int] = mapped_column(primary_key=True, autoincrement=True)
    telegram_id: Mapped[int] = mapped_column(BigInteger())
    chat_id: Mapped[int] = mapped_column(BigInteger())
    created_at: Mapped[datetime] = mapped_column(server_default=text("TIMEZONE('utc', now())"))
