package com.M2D.EmissionAssessment.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryStatusDtoList {

   private List<InventoryStatusDto> inventoryStatusDtoList;

}