package voltaprotocol.content;

import arc.util.Log;
import mindustry.content.*;
import mindustry.content.TechTree.TechNode;
import mindustry.type.ItemStack;

public class VPTechTree {

    public static void load() {
        Log.info("[Volta Protocol] Acoplando ramas de investigación al árbol global...");

        VPBlocks.silverWall.researchCost       = ItemStack.with(VPItems.silver, 150);
        VPBlocks.silverWallLarge.researchCost  = ItemStack.with(VPItems.silver, 350);
        VPBlocks.palladiumWall.researchCost    = ItemStack.with(VPItems.palladium, 200);
        VPBlocks.palladiumWallLarge.researchCost = ItemStack.with(VPItems.palladium, 450);
        VPBlocks.regenerativeWall.researchCost = ItemStack.with(VPItems.bioComposite, 250, VPItems.palladium, 100);
        VPBlocks.regenerativeWallLarge.researchCost = ItemStack.with(VPItems.bioComposite, 600, VPItems.palladium, 300);

        VPBlocks.moduleBasic.researchCost = ItemStack.with(VPItems.silver, 500);

        VPBlocks.liquidCargoLoader.researchCost =
            ItemStack.with(VPItems.silver, 600, VPItems.palladium, 400);
        VPBlocks.liquidCargoUnloadPoint.researchCost =
            ItemStack.with(VPItems.silver, 225, VPItems.palladium, 100);

        VPBlocks.moduleSilver.researchCost =
            ItemStack.with(VPItems.silver, 2000, VPItems.palladium, 500);

        VPBlocks.moduleRed.researchCost =
            ItemStack.with(VPItems.palladium, 1500, VPItems.silver, 1000);

        VPBlocks.moduleGreen.researchCost =
            ItemStack.with(VPItems.bioComposite, 1500, VPItems.silver, 2500);

        VPBlocks.moduleBlue.researchCost =
            ItemStack.with(VPItems.aegesium, 1500, VPItems.palladium, 800);

        VPBlocks.moduleOrange.researchCost =
            ItemStack.with(VPItems.voltium, 1500, VPItems.aegesium, 500);

        addToVanillaTree(Blocks.coreShard);
        Log.info("[Volta Protocol] ¡Árbol de Volta sincronizado!");
    }

    private static void addToVanillaTree(mindustry.world.Block coreRoot) {

        TechNode contextNode = TechTree.all.find(t -> t.content == coreRoot);
        if (contextNode == null) return;

        contextNode.children.add(
            TechTree.node(VPBlocks.voltaCore, () -> {

                // Ítems
                if (VPItems.silver != null) {
                    TechTree.nodeProduce(VPItems.silver, () -> {

                        if (VPItems.palladium != null) {
                            TechTree.nodeProduce(VPItems.palladium, () -> {

                                if (VPItems.voltium != null)
                                    TechTree.nodeProduce(VPItems.voltium, () -> {});

                                if (VPItems.bioComposite != null)
                                    TechTree.nodeProduce(VPItems.bioComposite, () -> {});
                            });
                        }

                        if (VPItems.aegesium != null)
                            TechTree.nodeProduce(VPItems.aegesium, () -> {});

                        if (VPLiquids.oxychloride != null) {
                            TechTree.nodeProduce(VPLiquids.oxychloride, () -> {

                                if (VPLiquids.bioPlasma != null) {
                                    TechTree.nodeProduce(VPLiquids.bioPlasma, () -> {

                                        if (VPLiquids.fluxPhase != null)
                                            TechTree.nodeProduce(VPLiquids.fluxPhase, () -> {});
                                    });
                                }
                            });
                        }
                    });
                }
                //Walls
                if (VPBlocks.silverWall != null) {
                    TechTree.node(VPBlocks.silverWall, () -> {

                        if (VPBlocks.silverWallLarge != null)
                            TechTree.node(VPBlocks.silverWallLarge);

                        if (VPBlocks.palladiumWall != null) {
                            TechTree.node(VPBlocks.palladiumWall, () -> {

                                if (VPBlocks.palladiumWallLarge != null) {
                                    TechTree.node(VPBlocks.palladiumWallLarge, () -> {

                                        if (VPBlocks.regenerativeWall != null) {
                                            TechTree.node(VPBlocks.regenerativeWall, () -> {

                                                if (VPBlocks.regenerativeWallLarge != null)
                                                    TechTree.node(VPBlocks.regenerativeWallLarge);
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                //Modulos y Liquid-Cargo
                if (VPBlocks.moduleBasic != null) {
                    TechTree.node(VPBlocks.moduleBasic, () -> {

                        if (VPBlocks.liquidCargoLoader != null) {
                            TechTree.node(VPBlocks.liquidCargoLoader, () -> {

                                if (VPBlocks.liquidCargoUnloadPoint != null)
                                    TechTree.node(VPBlocks.liquidCargoUnloadPoint);
                            });
                        }

                        if (VPBlocks.moduleSilver != null) {
                            TechTree.node(VPBlocks.moduleSilver, () -> {

                                if (VPBlocks.moduleRed != null) {
                                    TechTree.node(VPBlocks.moduleRed, () -> {

                                        if (VPBlocks.moduleBlue != null)
                                            TechTree.node(VPBlocks.moduleBlue);
                                    });
                                }
                                if (VPBlocks.moduleGreen != null) {
                                    TechTree.node(VPBlocks.moduleGreen, () -> {

                                        if (VPBlocks.moduleOrange != null)
                                            TechTree.node(VPBlocks.moduleOrange);
                                    });
                                }
                            });
                        }
                    });
                }
            })
        );
    }
}
