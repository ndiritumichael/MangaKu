//
//  DetailViewModel.swift
//  iosApp
//
//  Created by Uwais Alqadri on 14/09/21.
//  Copyright © 2021 Uwais Alqadri. All rights reserved.
//

import Foundation
import Shared
import KMPNativeCoroutinesAsync
import Combine

@MainActor
class DetailViewModel: ObservableObject {

  @Published var manga: ViewState<Manga> = .initiate
  @LazyKoin private var detailUseCase: DetailUseCase

  private var cancellables = Set<AnyCancellable>()

  func fetchManga(mangaId: String) {
    Task {
      manga = .loading
      do {
        let nativeFlow = try await asyncFunction(for: detailUseCase.getDetailMangaNative(mangaId: mangaId))
        let stream = asyncStream(for: nativeFlow)
        for try await value in stream {
          if let manga = value, value != nil {
            self.manga = .success(data: manga)
          } else {
            self.manga = .empty
          }
        }
      } catch {
        manga = .error(error: error)
      }
    }
  }

}
