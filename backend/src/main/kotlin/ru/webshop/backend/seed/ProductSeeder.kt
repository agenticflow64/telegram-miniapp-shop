package ru.webshop.backend.seed

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.beans.factory.annotation.Value
import org.springframework.mock.web.MockMultipartFile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import ru.webshop.backend.entity.Role
import ru.webshop.backend.repository.*
import ru.webshop.backend.service.interfaces.*
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ThreadLocalRandom
import kotlin.io.path.exists
import kotlin.io.path.extension
import kotlin.io.path.name

@Component
@Order(1)
class ProductSeeder(
    private val roleRepository: RoleRepository,
    private val categoryService: CategoryService,
    private val attributeService: AttributeService,
    private val productService: ProductService,
    private val photoUploadService: PhotoUploadService,
    private val productRepository: ProductRepository,
    @Value("\${app.seed.images-dir:}") private val imagesDir: String,
) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(javaClass)
    private val rnd get() = ThreadLocalRandom.current()
    private val imgDir: Path? = imagesDir.takeIf { it.isNotBlank() }?.let { Paths.get(it) }

    @Transactional
    override fun run(vararg args: String) {
        listOf(
            Role(roleName = "USER",  description = "Usual user"),
            Role(roleName = "ADMIN", description = "Admin can add products"),
        ).forEach { role ->
            if (!roleRepository.existsByRoleName(role.roleName)) {
                roleRepository.save(role)
                log.info("🔑  Роль «{}» создана", role.roleName)
            }
        }

        categoriesToCreate.forEach { (catName, attrs) ->
            val category = if (categoryService.existsByCategoryName(catName))
                categoryService.getByCategoryName(catName)
            else
                categoryService.createCategory(catName).also {
                    log.info("📂  Категория «{}» создана", catName)
                }

            attrs.forEach { attr ->
                if (!attributeService.attributeExists(category.id, attr)) {
                    attributeService.createAttributeToCategory(category.id, attr)
                    log.debug("     └─ атрибут «{}» добавлен", attr)
                }
            }
        }

        if (productRepository.count() != 0L){
            return;
        }

        seedProducts.forEach { p ->
            val categoryId = categoryService.getByCategoryName(p.category).id

            val article = productService.createProduct(
                ru.webshop.backend.dto.ProductCreateRequestDTO(
                    name            = p.name,
                    price           = BigDecimal.valueOf(p.price),
                    balanceInStock  = p.balance,
                    description     = p.description,
                    categoryId      = categoryId,
                    attributes      = p.attributes,
                    numberOfOrders  = p.numberOfOrders,
                    rating          = BigDecimal.valueOf(p.rating),
                )
            )
            log.info("🆕  «{}» создан (артикул {})", p.name, article)

            val dir = imgDir
            if (dir == null) {
                if (p.photos.isNotEmpty()) log.info("Каталог изображений не задан (SEED_IMAGES_DIR), товар «{}» без фото.", p.name)
            } else p.photos.forEachIndexed { idx, fname ->
                val path = dir.resolve(fname)
                if (!path.exists()) {
                    log.warn("⚠️  Файл {} отсутствует, пропуск.", path)
                    return@forEachIndexed
                }
                try {
                    val mf = path.toMultipart()
                    photoUploadService.uploadPhotoForProduct(article, mf, idx)
                    log.debug("     └─ 📷 {} загружено", fname)
                } catch (e: Exception) {
                    log.error("❌  Не удалось загрузить {}: {}", fname, e.message)
                }
            }
        }

        log.info("✅  Сидирование завершено: создано {} товаров.", seedProducts.size)
    }

    private fun Path.toMultipart(): MultipartFile {
        val bytes = Files.readAllBytes(this)
        val media = when (extension.lowercase()) {
            "png"        -> "image/png"
            "jpg", "jpeg"-> "image/jpeg"
            else         -> "application/octet-stream"
        }
        return MockMultipartFile(name, name, media, bytes)
    }

    private val categoriesToCreate: List<Pair<String, List<String>>> = listOf(
        "Тетрадь"    to listOf("Производитель","Количество листов","Коллекция","Разметка","Кол-во в упаковке"),
        "Ежедневник" to listOf("Производитель","Цвет","Коллекция","Количество листов","Кол-во в упаковке"),
        "Ластик"     to listOf("Производитель","Тип","Коллекция","Цвет","Кол-во в упаковке"),
        "Ланъярд"    to listOf("Производитель","Цвет","Коллекция","Материал","Кол-во в упаковке"),
        "Блокнот"    to listOf("Производитель","Формат","Коллекция","Количество листов","Кол-во в упаковке"),
        "Ручка"      to listOf("Производитель","Цвет чернил","Цвет ручки","Коллекция","Толщина стержня","Кол-во в упаковке"),
        "Значок"     to listOf("Производитель","Цвет","Коллекция","Вид замка","Кол-во в упаковке"),
        "Набор"      to listOf("Производитель","Цвет","Коллекция","Кол-во в упаковке"),
    )

    private data class SeedProduct(
        val name: String,
        val category: String,
        val price: Double,
        val balance: Long,
        val numberOfOrders: Long,
        val rating: Double,
        val description: String,
        val attributes: Map<String, String>,
        val photos: List<String>
    )

    private val seedProducts = listOf(
        SeedProduct(
            name = "Тетрадь в клетку 12 листов",
            category = "Тетрадь",
            price = 50.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Тетрадь в клетку из 12 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Количество листов" to "12",
                "Коллекция" to "СГУ для студентов",
                "Разметка" to "Клетка",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("copybook-cage1.png", "copybook-cage2.png", "copybook-cage3.png")
        ),
        SeedProduct(
            name = "Тетрадь в клетку 24 листа",
            category = "Тетрадь",
            price = 70.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Тетрадь в клетку из 24-х листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Количество листов" to "24",
                "Коллекция" to "СГУ для студентов",
                "Разметка" to "Клетка",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("copybook-cage1.png", "copybook-cage2.png", "copybook-cage3.png")
        ),
        SeedProduct(
            name = "Тетрадь в клетку 48 листов",
            category = "Тетрадь",
            price = 100.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Тетрадь в клетку из 48 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Количество листов" to "48",
                "Коллекция" to "СГУ для студентов",
                "Разметка" to "Клетка",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("copybook-cage1.png", "copybook-cage2.png", "copybook-cage3.png")
        ),
        SeedProduct(
            name = "Тетрадь в линейку 12 листов",
            category = "Тетрадь",
            price = 50.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Тетрадь в линейку из 12 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Количество листов" to "12",
                "Коллекция" to "СГУ для студентов",
                "Разметка" to "Линейка",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("copybook-ruler1.png", "copybook-ruler2.png", "copybook-ruler3.png")
        ),
        SeedProduct(
            name = "Тетрадь в линейку 24 листа",
            category = "Тетрадь",
            price = 70.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Тетрадь в линейку из 24-х листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Количество листов" to "24",
                "Коллекция" to "СГУ для студентов",
                "Разметка" to "Линейка",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("copybook-ruler1.png", "copybook-ruler2.png", "copybook-ruler3.png")
        ),
        SeedProduct(
            name = "Тетрадь в линейку 48 листов",
            category = "Тетрадь",
            price = 100.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Тетрадь в линейку из 48 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Количество листов" to "48",
                "Коллекция" to "СГУ для студентов",
                "Разметка" to "Линейка",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("copybook-ruler1.png", "copybook-ruler2.png", "copybook-ruler3.png")
        ),
        SeedProduct(
            name = "Набор тетрадей в клетку 12 листов",
            category = "Тетрадь",
            price = 120.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор тетрадей в клетку из 12 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Количество листов" to "12",
                "Коллекция" to "СГУ для студентов",
                "Разметка" to "Клетка",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("copybook-cage1.png", "copybook-cage2.png", "copybook-cage3.png")
        ),
        SeedProduct(
            name = "Набор тетрадей в клетку 24 листа",
            category = "Тетрадь",
            price = 180.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор тетрадей в клетку из 24-х листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Количество листов" to "24",
                "Коллекция" to "СГУ для студентов",
                "Разметка" to "Клетка",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("copybook-cage1.png", "copybook-cage2.png", "copybook-cage3.png")
        ),
        SeedProduct(
            name = "Набор тетрадей в клетку 48 листов",
            category = "Тетрадь",
            price = 270.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор тетрадей в клетку из 24-х листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Количество листов" to "48",
                "Коллекция" to "СГУ для студентов",
                "Разметка" to "Клетка",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("copybook-cage1.png", "copybook-cage2.png", "copybook-cage3.png")
        ),
        SeedProduct(
            name = "Набор тетрадей в линейку 12 листов",
            category = "Тетрадь",
            price = 120.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор тетрадей в клетку из 12 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Количество листов" to "12",
                "Коллекция" to "СГУ для студентов",
                "Разметка" to "Линейка",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("copybook-ruler1.png", "copybook-ruler2.png", "copybook-ruler3.png")
        ),
        SeedProduct(
            name = "Набор тетрадей в линейку 24 листа",
            category = "Тетрадь",
            price = 180.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор тетрадей в клетку из 24-х листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Количество листов" to "24",
                "Коллекция" to "СГУ для студентов",
                "Разметка" to "Линейка",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("copybook-ruler1.png", "copybook-ruler2.png", "copybook-ruler3.png")
        ),
        SeedProduct(
            name = "Набор тетрадей в линейку 48 листов",
            category = "Тетрадь",
            price = 270.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор тетрадей в клетку из 48 листов с логотипом СГУ на лицевой стороне обложки и рекламной информацией на другой стороне",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Количество листов" to "48",
                "Коллекция" to "СГУ для студентов",
                "Разметка" to "Линейка",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("copybook-ruler1.png", "copybook-ruler2.png", "copybook-ruler3.png")
        ),
        SeedProduct(
            name = "Ежедневник синий",
            category = "Ежедневник",
            price = 400.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ежедневник с логотипом СГУ и надписью на корешке в синей расцветке с резинкой",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Синий",
                "Коллекция" to "СГУ для студентов",
                " Количество листов " to "128",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("datebook-blue.png")
        ),
        SeedProduct(
            name = "Ежедневник белый",
            category = "Ежедневник",
            price = 400.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ежедневник с логотипом СГУ и надписью на корешке в белой расцветке с резинкой",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Белый",
                "Коллекция" to "СГУ для студентов",
                " Количество листов " to "128",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("datebook-white.png")
        ),
        SeedProduct(
            name = "Набор ежедневников",
            category = "Ежедневник",
            price = 1000.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ежедневники с логотипом СГУ и надписью на корешке в белой и синей расцветке с резинкой",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Белый и синий",
                "Коллекция" to "СГУ для студентов",
                " Количество листов " to "128",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("datebook-set.png")
        ),
        SeedProduct(
            name = "Ластик мягкий",
            category = "Ластик",
            price = 50.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Мягкий ластик, подходит для работы с тонкой бумагой и простым карандашом",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Тип" to "Мягкий",
                "Коллекция" to "СГУ для студентов",
                "Цвет" to "Белый",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("eraser.png")
        ),
        SeedProduct(
            name = "Ластик твердый",
            category = "Ластик",
            price = 50.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Твердый ластик, подходит для работы с плотной бумагой",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Тип" to "Твердый",
                "Коллекция" to "СГУ для студентов",
                "Цвет" to "Белый",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("eraser.png")
        ),
        SeedProduct(
            name = "Ластик для стирания чернил",
            category = "Ластик",
            price = 70.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ластик подходит для стирания любых видов чернил, рекомендуется применять только на плотной бумаге",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Тип" to "Для чернил",
                "Коллекция" to "СГУ для студентов",
                "Цвет" to "Белый",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("eraser.png")
        ),
        SeedProduct(
            name = "Набор мягких ластиков",
            category = "Ластик",
            price = 120.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Мягкие ластики, подходят для работы с тонкой бумагой и простым карандашом",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Тип" to "Мягкий",
                "Коллекция" to "СГУ для студентов",
                "Цвет" to "Разноцветные",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("erasers.png")
        ),
        SeedProduct(
            name = "Набор твердых ластиков",
            category = "Ластик",
            price = 120.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Твердые ластики, подходят для работы с плотной бумагой",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Тип" to "Твердый",
                "Коллекция" to "СГУ для студентов",
                "Цвет" to "Разноцветные",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("erasers.png")
        ),
        SeedProduct(
            name = "Ланъярд розовый",
            category = "Ланъярд",
            price = 70.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ланъярд для бейджей и пропускных карт, в розовом цвете",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Розовый",
                "Коллекция" to "СГУ – наш выбор",
                "Материал" to "Репс",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("lanyard-pink.png")
        ),
        SeedProduct(
            name = "Ланъярд синий",
            category = "Ланъярд",
            price = 70.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ланъярд для бейджей и пропускных карт, в синем цвете",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Синий",
                "Коллекция" to "СГУ – наш выбор",
                "Материал" to "Репс",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("lanyard-blue.png")
        ),
        SeedProduct(
            name = "Блокнот А6 60 листов",
            category = "Блокнот",
            price = 100.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Блокнот для записей с логотипом СГУ, формат А6, 60 листов",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Формат" to "А6",
                "Коллекция" to "СГУ – наш выбор",
                "Количество листов" to "60",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("notebook1.png", "notebook2.png")
        ),
        SeedProduct(
            name = "Блокнот А6 100 листов",
            category = "Блокнот",
            price = 150.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Блокнот для записей с логотипом СГУ, формат А6, 100 листов",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Формат" to "А6",
                "Коллекция" to "СГУ – наш выбор",
                "Количество листов" to "100",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("notebook1.png", "notebook2.png")
        ),
        SeedProduct(
            name = "Блокнот А5 40 листов",
            category = "Блокнот",
            price = 70.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Блокнот для записей с логотипом СГУ, формат А5, 40 листов",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Формат" to "А5",
                "Коллекция" to "СГУ – наш выбор",
                "Количество листов" to "40",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("notepad1.png", "notepad2.png")
        ),
        SeedProduct(
            name = "Блокнот А5 80 листов",
            category = "Блокнот",
            price = 120.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Блокнот для записей с логотипом СГУ, формат А5, 80 листов",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Формат" to "А5",
                "Коллекция" to "СГУ – наш выбор",
                "Количество листов" to "80",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("notepad1.png", "notepad2.png")
        ),
        SeedProduct(
            name = "Ручка бирюзовая, синие чернила",
            category = "Ручка",
            price = 40.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ручка бирюзово-синего цвета с синими чернилами",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет чернил" to "Синий",
                "Цвет ручки" to "Бирюзовый",
                "Коллекция" to "СГУ – наш выбор",
                "Толщина стержня" to "0.5мм",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("pen-blue.png")
        ),
        SeedProduct(
            name = "Ручка фиолетовая, синие чернила",
            category = "Ручка",
            price = 40.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ручка фиолетово-синего цвета с синими чернилами",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет чернил" to "Синий",
                "Цвет ручки" to "Фиолетовый",
                "Коллекция" to "СГУ – наш выбор",
                "Толщина стержня" to "0.5мм",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("pen-violet.png")
        ),
        SeedProduct(
            name = "Ручка желтая, синие чернила",
            category = "Ручка",
            price = 40.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ручка желто-синего цвета с синими чернилами",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет чернил" to "Синий",
                "Цвет ручки" to "Желтый",
                "Коллекция" to "СГУ – наш выбор",
                "Толщина стержня" to "0.5мм",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("pen-yellow.png")
        ),
        SeedProduct(
            name = "Набор ручек, синие чернила",
            category = "Ручка",
            price = 100.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор разноцветных ручек с синими чернилами",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет чернил" to "Синий",
                "Цвет ручки" to "Разноцветные",
                "Коллекция" to "СГУ – наш выбор",
                "Толщина стержня" to "0.5мм",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("pen-set.png")
        ),
        SeedProduct(
            name = "Ручка бирюзовая, черные чернила",
            category = "Ручка",
            price = 40.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ручка бирюзово-синего цвета с черными чернилами",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет чернил" to "Черный",
                "Цвет ручки" to "Бирюзовый",
                "Коллекция" to "СГУ – наш выбор",
                "Толщина стержня" to "0.5мм",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("pen-blue.png")
        ),
        SeedProduct(
            name = "Ручка фиолетовая, черные чернила",
            category = "Ручка",
            price = 40.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ручка фиолетово-синего цвета с черными чернилами",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет чернил" to "Черный",
                "Цвет ручки" to "Фиолетовый",
                "Коллекция" to "СГУ – наш выбор",
                "Толщина стержня" to "0.5мм",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("pen-violet.png")
        ),
        SeedProduct(
            name = "Ручка желтая, черные чернила",
            category = "Ручка",
            price = 40.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ручка желто-синего цвета с черными чернилами",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет чернил" to "Черный",
                "Цвет ручки" to "Желтый",
                "Коллекция" to "СГУ – наш выбор",
                "Толщина стержня" to "0.5мм",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("pen-yellow.png")
        ),
        SeedProduct(
            name = "Набор ручек, черные чернила",
            category = "Ручка",
            price = 100.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор разноцветных ручек с черными чернилами",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет чернил" to "Черный",
                "Цвет ручки" to "Разноцветные",
                "Коллекция" to "СГУ – наш выбор",
                "Толщина стержня" to "0.5мм",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("pen-set.png")
        ),
        SeedProduct(
            name = "Набор ручек «Юбилей СГУ», черные чернила",
            category = "Ручка",
            price = 120.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор разноцветных ручек из коллекции «Юбилей СГУ» с черными чернилами",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет чернил" to "Черный",
                "Цвет ручки" to "Разноцветные",
                "Коллекция" to "Юбилей СГУ",
                "Толщина стержня" to "0.5мм",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("pen-another.png")
        ),
        SeedProduct(
            name = "Набор ручек «Юбилей СГУ», синие чернила",
            category = "Ручка",
            price = 120.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор разноцветных ручек из коллекции «Юбилей СГУ» с синими чернилами",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет чернил" to "Синий",
                "Цвет ручки" to "Разноцветные",
                "Коллекция" to "Юбилей СГУ",
                "Толщина стержня" to "0.5мм",
                "Кол-во в упаковке" to "3"
            ),
            photos = listOf("pen-another.png")
        ),
        SeedProduct(
            name = "Значок синий",
            category = "Значок",
            price = 50.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Значок синего цвета с логотипом СГУ",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Синий",
                "Коллекция" to "Юбилей СГУ",
                "Вид замка" to "булавка",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("pin-blue.png")
        ),
        SeedProduct(
            name = "Значок белый",
            category = "Значок",
            price = 50.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Значок белого цвета с логотипом СГУ",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Белый",
                "Коллекция" to "Юбилей СГУ",
                "Вид замка" to "булавка",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("pin-white.png")
        ),
        SeedProduct(
            name = "Набор значков",
            category = "Значок",
            price = 170.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Значки синего и белого цвета с логотипом СГУ",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Белый и синий",
                "Коллекция" to "Юбилей СГУ",
                "Вид замка" to "булавка",
                "Кол-во в упаковке" to "4"
            ),
            photos = listOf("pin-set.png")
        ),

        SeedProduct(
            name = "Набор ежедневник + ручка, синий цвет",
            category = "Набор",
            price = 450.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ежедневник синего цвета с логотипом СГУ и ручка с синими чернилами",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Синий",
                "Коллекция" to "СГУ для студентов",
                "Количество листов" to "128",
                "Разметка" to "Линейка",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("set-blue.png")
        ),
        SeedProduct(
            name = "Набор ежедневник + ручка, белый цвет",
            category = "Набор",
            price = 450.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Ежедневник белого цвета с логотипом СГУ и ручка с синими чернилами",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Белый",
                "Коллекция" to "СГУ для студентов",
                "Количество листов" to "128",
                "Разметка" to "Линейка",
                "Кол-во в упаковке" to "1"
            ),
            photos = listOf("set-white.png")
        ),
        SeedProduct(
            name = "Набор стикеров «Виды СГУ»",
            category = "Набор",
            price = 100.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор стикеров «Виды СГУ» из 4-х стикеров",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Разноцветные",
                "Коллекция" to "Юбилей СГУ",
                "Кол-во в упаковке" to "4"
            ),



            photos = listOf("stickers-version1.png")
        ),
        SeedProduct(
            name = "Набор стикеров «Наш СГУ»",
            category = "Набор",
            price = 100.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор стикеров «Наш СГУ» из 4-х стикеров",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Разноцветные",
                "Коллекция" to "СГУ – наш выбор",
                "Кол-во в упаковке" to "4"
            ),
            photos = listOf("stickers-version2.png")
        ),
        SeedProduct(
            name = "Набор стикеров «Любимый СГУ»",
            category = "Набор",
            price = 100.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор стикеров «Любимый СГУ» из 4-х стикеров",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Разноцветные",
                "Коллекция" to "СГУ – наш выбор",
                "Кол-во в упаковке" to "4"
            ),
            photos = listOf("stickers-version3.png")
        ),
        SeedProduct(
            name = "Полный набор стикеров",
            category = "Набор",
            price = 180.0,
            balance = rnd.nextLong(100, 301),
            numberOfOrders = rnd.nextLong(100, 301),
            rating = rnd.nextDouble(4.0, 5.0),
            description = "Набор стикеров, посвященных СГУ, 8 штук",
            attributes = mapOf(
                "Производитель" to "СГУ",
                "Цвет" to "Разноцветные",
                "Коллекция" to "СГУ для студентов",
                "Кол-во в упаковке" to "8"
            ),
            photos = listOf("stickers-set.png")
        )
    )
}
