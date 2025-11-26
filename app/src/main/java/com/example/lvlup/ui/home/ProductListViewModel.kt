import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.example.lvlup.data.ProductoDto
import com.example.lvlup.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductListViewModel(private val repo: ProductRepository): ViewModel() {
    // ⚠️ CAMBIO 1: Inicializar la categoría a "Todas" en lugar de ""
    var category by mutableStateOf("Todas")

    val productsFlow: Flow<List<ProductoDto>>
        // ⚠️ CAMBIO 2: Si la categoría es "Todas", llamar a repo.getProducts() para obtener todos.
        get() = if (category == "Todas") repo.getProducts() else repo.getProductsByCategory(category)
}