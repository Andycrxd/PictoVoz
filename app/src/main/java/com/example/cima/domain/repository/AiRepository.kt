package com.example.cima.domain.repository

interface AiRepository {
    /**
     * Procesa una lista de etiquetas de pictogramas y genera una oración coherente.
     * Si la IA falla o no está inicializada, retorna la concatenación por defecto (Fallback).
     */
    suspend fun generateSentence(pictograms: List<String>): Result<String>
}