import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.example.lvlup.data.ProductEntity
import com.example.lvlup.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductListViewModel(private val repo: ProductRepository): ViewModel() {
    var category by mutableStateOf("")

    val productsFlow: Flow<List<ProductEntity>>
        get() = if (category.isEmpty()) repo.getProducts() else repo.getProductsByCategory(category)
}
