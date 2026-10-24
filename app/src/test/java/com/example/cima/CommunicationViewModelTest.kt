package com.example.cima

import com.example.cima.data.model.Categoria
import com.example.cima.data.model.Pictogram
import com.example.cima.data.repository.PictogramRepository
import com.example.cima.domain.SentenceBuilder
import com.example.cima.domain.TextToSpeechManager
import com.example.cima.viewmodel.CommunicationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class CommunicationViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    // Mocks creados mediante la herramienta Mockito
    private val mockRepository: PictogramRepository = mock()
    private val mockSentenceBuilder: SentenceBuilder = mock()
    private val mockTtsManager: TextToSpeechManager = mock()

    private lateinit var viewModel: CommunicationViewModel

    private val mockCategories = listOf(
        Categoria("needs", "Necesidades"),
        Categoria("emotions", "Emociones")
    )

    private val mockPictograms = listOf(
        Pictogram("needs_water", "Agua", "needs"),
        Pictogram("needs_food", "Comida", "needs"),
        Pictogram("needs_bathroom", "Baño", "needs"),
        Pictogram("needs_sleep", "Dormir", "needs"),
        Pictogram("needs_help", "Ayuda", "needs"),
        Pictogram("needs_pain", "Dolor", "needs")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Configuración inicial de comportamiento para los Mocks (Stubbing)
        whenever(mockRepository.getCategories()).thenReturn(mockCategories)
        whenever(mockRepository.getPictogramsByCategory("needs")).thenReturn(mockPictograms)
        whenever(mockRepository.getPictograms()).thenReturn(mockPictograms)
        whenever(mockRepository.getQuickPhrases()).thenReturn(emptyList())

        viewModel = CommunicationViewModel(
            repository = mockRepository,
            sentenceBuilder = mockSentenceBuilder,
            textToSpeechManager = mockTtsManager
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // CASO DE PRUEBA 1: Verificar el procesamiento de la oración con IA y la reproducción por TTS
    @Test
    fun `speakCurrentSentence debe llamar a SentenceBuilder y reproduce audio mediante TTS`() = runTest {
        // Dado un pictograma seleccionado en la barra
        val pictogram = mockPictograms[0]
        viewModel.addPictogram(pictogram)

        // Configuracion de respuesta mock para la IA
        whenever(mockSentenceBuilder.buildSentence(listOf(pictogram))).thenReturn("Quiero agua")

        // Cuando el usuario presiona el botón Hablar
        viewModel.speakCurrentSentence()
        testDispatcher.scheduler.advanceUntilIdle() // Ejecuta las corrutinas pendientes

        // Entonces
        val state = viewModel.uiState.value
        assertEquals("Quiero agua", state.generatedSentence)
        assertFalse(state.isProcessing)

        // Verificación con Mockito: el gestor de síntesis de voz debió ser invocado
        verify(mockTtsManager).speak("Quiero agua")
    }

    // CASO DE PRUEBA 2: Validar el bloqueo por límite máximo de 5 pictogramas (RN1)
    @Test
    fun `addPictogram no debe agregar mas de 5 pictogramas e incrementa el contador de advertencia`() {
        // Dado que agregamos 5 pictogramas a la barra (límite máximo)
        repeat(5) { index ->
            viewModel.addPictogram(mockPictograms[index])
        }

        // Intento de agregar un sexto pictograma
        viewModel.addPictogram(mockPictograms[5])

        // Entonces la lista se mantiene en 5 y se activa el indicador de advertencia
        val state = viewModel.uiState.value
        assertEquals(5, state.selectedPictograms.size)
        assertEquals("La barra ya tiene 5 pictogramas.", state.feedbackMessage)
        assertEquals(1, state.limitWarningCounter)
    }

    // CASO DE PRUEBA 3: Verificar que el botón Hablar no procese si la barra está vacía (RN12)
    @Test
    fun `speakCurrentSentence no ejecuta procesamiento ni invoca a TTS si la barra esta vacia`() = runTest {
        // Dado que la barra está vacía
        assertTrue(viewModel.uiState.value.selectedPictograms.isEmpty())

        // Cuando se intenta presionar Hablar
        viewModel.speakCurrentSentence()
        testDispatcher.scheduler.advanceUntilIdle()

        // Verificación con Mockito: Nunca se debió llamar al SentenceBuilder ni al TTS
        verify(mockSentenceBuilder, never()).buildSentence(any())
        verify(mockTtsManager, never()).speak(any())
    }

    // CASO DE PRUEBA 4: Limpieza total de la barra de construcción (RF2.2)
    @Test
    fun `clearSentence debe vaciar los pictogramas seleccionados y resetear la frase`() {
        // Dado que hay pictogramas agregados
        viewModel.addPictogram(mockPictograms[0])
        viewModel.addPictogram(mockPictograms[1])

        // Cuando se invoca limpiar la barra
        viewModel.clearSentence()

        // Entonces
        val state = viewModel.uiState.value
        assertTrue(state.selectedPictograms.isEmpty())
        assertEquals("", state.generatedSentence)
        assertEquals("Barra limpia.", state.feedbackMessage)
    }

    // CASO DE PRUEBA 5: Filtrado correcto de pictogramas en la búsqueda personalizada
    @Test
    fun `onSearchKeyInput filtra la lista de pictogramas de acuerdo a la consulta`() {
        // Dado que se activa la búsqueda
        viewModel.toggleSearch()

        // Cuando el usuario escribe "Agua" mediante el teclado simplificado
        viewModel.onSearchKeyInput("A")
        viewModel.onSearchKeyInput("g")
        viewModel.onSearchKeyInput("u")
        viewModel.onSearchKeyInput("a")

        // Entonces la lista de pictogramas mostrada debe contener solo "Agua"
        val state = viewModel.uiState.value
        assertEquals("Agua", state.searchQuery)
        assertEquals(1, state.pictograms.size)
        assertEquals("Agua", state.pictograms.first().nombre)
    }
}