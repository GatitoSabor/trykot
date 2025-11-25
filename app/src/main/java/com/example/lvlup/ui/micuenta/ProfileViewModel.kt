import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lvlup.data.AddressEntity
import com.example.lvlup.data.CouponEntity
import com.example.lvlup.data.UserEntity
import com.example.lvlup.repository.CouponRepository
import com.example.lvlup.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val couponRepository: CouponRepository,
    userId: Int
) : ViewModel() {

    var usuario by mutableStateOf<UserEntity?>(null)
    var direcciones by mutableStateOf(listOf<AddressEntity>())
    var editarNombre by mutableStateOf(false)
    var editarEmail by mutableStateOf(false)
    var newAddressText by mutableStateOf("")
    var editandoDireccionId by mutableStateOf<Int?>(null)
    var tempNombre by mutableStateOf("")
    var tempEmail by mutableStateOf("")
    var cupones by mutableStateOf(listOf<CouponEntity>())

    private var currentUserId: Int = userId

    init {
        cargarUsuarioCompleto(userId)
    }

    fun cargarUsuarioCompleto(nuevoUserId: Int) {
        currentUserId = nuevoUserId
        cargarDatosUsuario()
        cargarDirecciones()
        cargarMisCupones(nuevoUserId)
        editarNombre = false
        editarEmail = false
        editandoDireccionId = null
        tempNombre = ""
        tempEmail = ""
        newAddressText = ""
    }

    fun cargarDatosUsuario() {
        viewModelScope.launch {
            usuario = userRepository.getUserById(currentUserId)
        }
    }

    fun cargarDirecciones() {
        viewModelScope.launch {
            direcciones = userRepository.getAddresses(currentUserId)
        }
    }

    fun cargarMisCupones(userId: Int = currentUserId) {
        viewModelScope.launch {
            cupones = couponRepository.getUnusedCoupons(userId)
        }
    }

    fun limpiarDatosUsuario() {
        usuario = null
        direcciones = listOf()
        editarNombre = false
        editarEmail = false
        newAddressText = ""
        editandoDireccionId = null
        tempNombre = ""
        tempEmail = ""
        cupones = listOf()
        currentUserId = -1
    }

    fun startEditNombre() {
        tempNombre = usuario?.nombre ?: ""
        editarNombre = true
    }

    fun updateNombre(nuevoNombre: String) {
        usuario?.let {
            val actualizado = it.copy(nombre = nuevoNombre)
            viewModelScope.launch {
                userRepository.updateUser(actualizado)
                usuario = actualizado
                editarNombre = false
            }
        }
    }

    fun startEditEmail() {
        tempEmail = usuario?.email ?: ""
        editarEmail = true
    }

    fun updateEmail(nuevoEmail: String) {
        usuario?.let {
            val actualizado = it.copy(email = nuevoEmail)
            viewModelScope.launch {
                userRepository.updateUser(actualizado)
                usuario = actualizado
                editarEmail = false
            }
        }
    }

    fun updatePassword(nuevoPass: String) {
        usuario?.let {
            val actualizado = it.copy(password = nuevoPass)
            viewModelScope.launch {
                userRepository.updateUser(actualizado)
                usuario = actualizado
            }
        }
    }

    fun addAddress(text: String) {
        if (text.isNotBlank()) {
            viewModelScope.launch {
                userRepository.addAddress(currentUserId, text)
                cargarDirecciones()
                newAddressText = ""
            }
        }
    }

    fun removeAddress(address: AddressEntity) {
        viewModelScope.launch {
            userRepository.removeAddress(address)
            cargarDirecciones()
            if (editandoDireccionId == address.id) {
                editandoDireccionId = null
            }
        }
    }

    fun startEditDireccion(address: AddressEntity) {
        editandoDireccionId = address.id
    }

    fun updateDireccionInPlace(id: Int, newValue: String) {
        viewModelScope.launch {
            val direccion = direcciones.find { it.id == id }
            if (direccion != null) {
                val updatedDireccion = direccion.copy(value = newValue)
                userRepository.updateAddress(updatedDireccion)
                cargarDirecciones()
            }
        }
    }

    fun stopEditDireccion() {
        editandoDireccionId = null
    }
}
