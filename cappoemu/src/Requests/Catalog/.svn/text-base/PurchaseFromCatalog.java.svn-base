package Requests.Catalog;

import Server.Catalog.CatalogItem;
import Server.Catalog.CatalogPage;
import Server.Connection;
import Server.DatabaseClient;
import Server.Furniture.Item;
import Server.Pet;
import Server.UserItem;
import Requests.Handler;
import Server.Server;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 *****************
 * @author capos *
 *****************
*/

public class PurchaseFromCatalog extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int PageId = Main.DecodeInt();

        CatalogPage page = Environment.Catalog.Pages.get(PageId);
        if (page == null)
        {
            Environment.InitPacket(296, Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }
        if (!page.Enabled || !page.Visible)
        {
            Environment.InitPacket(296, Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }
        if (page.StaffOnly && !Main.Data.IsStaff())
        {
            Environment.InitPacket(296, Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        int ItemId = Main.DecodeInt();

        if (!page.Caption.equals("Habbo Club / VIP"))
        {
            CatalogItem item = page.Items.get(ItemId);
            if (item == null)
            {
                return;
            }

            if(item.MinRank > 0)
            {
                if(Main.Data.Subscription.Type >= item.MinRank) // Hc or Vip
                {
                    if(!Main.CheckSubscription())
                    {
                        Environment.InitPacket(296, Main.ClientMessage);
                        Environment.Append(1, Main.ClientMessage);
                        Environment.EndPacket(Main.Socket, Main.ClientMessage);
                        return;
                    }
                }
                else
                {
                    Environment.InitPacket(296, Main.ClientMessage);
                    Environment.Append(1, Main.ClientMessage);
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                    return;
                }
            }

            Item Item = Environment.Furniture.Items.get(item.Id);
            if (Item == null)
            {
                return;
            }

            if (Main.Data.Credits < item.Credits)
            {
                Environment.InitPacket(68, Main.ClientMessage);
                Environment.Append(true, Main.ClientMessage); // is credits problem
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
                return;
            }

            if (item.ExtraCost == 4)
            {
                if (Main.Data.Shells < item.Pixels)
                {
                    Environment.InitPacket(68, Main.ClientMessage);
                    Environment.Append(false, Main.ClientMessage); // is credits problem
                    Environment.Append(true, Main.ClientMessage); // is pixels, hearts, etc.. problem
                    Environment.Append(4, Main.ClientMessage); // type (shells)
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                    return;
                }
            }
            else
            {
                if (Main.Data.Pixels < item.Pixels)
                {
                    Environment.InitPacket(68, Main.ClientMessage);
                    Environment.Append(false, Main.ClientMessage); // is credits problem
                    Environment.Append(true, Main.ClientMessage); // is pixels, hearts, etc.. problem
                    Environment.Append(0, Main.ClientMessage); // type (pixels)
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                    return;
                }
            }

            String ExtraData = Main.DecodeString();
            if(page.Layout.equals("pets"))
            {
                String[] strArray = ExtraData.split("\n");
                int NameLen = strArray[0].length();
                if(NameLen < 3 || NameLen > 15 || !ValidPetNameChars(strArray[0],NameLen))
                {
                    return;
                }
                int raceid;
                try
                {
                    raceid = Integer.parseInt(strArray[1]);
                }
                catch(NumberFormatException ex)
                {
                    return;
                }
                if (strArray[2].length() != 6)
                {
                    return;
                }


                int PetId = Environment.GenerateUserId();
                Pet pet = new Pet(PetId, Main.Data.Id, Main.Data.UserName, strArray[0], Integer.parseInt(Item.ItemName.substring(6)), raceid, strArray[2], Environment.GetTimestamp(), 50, 0, 100, 1, 0);
                
                Main.Data.InventoryPets.put(PetId, pet);
                
                Environment.InitPacket(603, Main.ClientMessage);
                pet.SerializeInventory(Environment, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);

                Environment.InitPacket(67, Main.ClientMessage);
                Environment.Append(Item.Id, Main.ClientMessage);
                Environment.Append(Item.PublicName, Main.ClientMessage);
                Environment.Append(item.Credits, Main.ClientMessage);
                Environment.Append(item.Pixels, Main.ClientMessage);
                Environment.Append(item.ExtraCost, Main.ClientMessage);
                Environment.Append(1, Main.ClientMessage); // Items Count
                Environment.Append(Item.Type, Main.ClientMessage); // type
                Environment.Append(Item.SpriteId, Main.ClientMessage); // sprite id
                Environment.Append("", Main.ClientMessage); // Controller
                Environment.Append(1, Main.ClientMessage); // Product Count
                Environment.Append(Environment.GetTimestamp(), Main.ClientMessage);
                Environment.Append(item.MinRank, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);

                if (item.Credits > 0)
                {
                    Main.Data.Credits -= item.Credits;
                    Environment.InitPacket(6, Main.ClientMessage);
                    Environment.Append(Integer.toString(Main.Data.Credits), Main.ClientMessage);
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                }
                if (item.Pixels > 0)
                {
                    Main.Data.Pixels -= item.Pixels;
                    Environment.InitPacket(438, Main.ClientMessage);
                    Environment.Append(Main.Data.Pixels, Main.ClientMessage);
                    Environment.Append(0, Main.ClientMessage);
                    Environment.Append(0, Main.ClientMessage); // Id Pixels
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                }
                
                // UnseenItems
                Environment.InitPacket(832, Main.ClientMessage);
                Environment.Append(1, Main.ClientMessage); // Count items
                Environment.Append(3, Main.ClientMessage); // Type PET
                Environment.Append(1, Main.ClientMessage); // Amount products
                Environment.Append(PetId, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
            }
            else
            {
                boolean IsDealBuy = false;
                if(item.ItemsSprite.size()>1)
                {
                    IsDealBuy = true;
                }

                // Update Inventory
                Environment.InitPacket(101, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);

                Environment.InitPacket(67, Main.ClientMessage);
                Environment.Append(Item.Id, Main.ClientMessage);
                Environment.Append(Item.PublicName, Main.ClientMessage);
                Environment.Append(item.Credits, Main.ClientMessage);
                Environment.Append(item.Pixels, Main.ClientMessage);
                Environment.Append(item.ExtraCost, Main.ClientMessage);
                Environment.Append(item.ItemsType.size(), Main.ClientMessage); // Items Count
                for(int e = 0;item.ItemsType.size() > e;e++)
                {
                    int SpriteId = item.ItemsSprite.get(e);
                    int Ammount = item.ItemsAmmount.get(e);
                    String Type = item.ItemsType.get(e);

                    Environment.Append(Type, Main.ClientMessage); // type
                    Environment.Append(SpriteId, Main.ClientMessage); // sprite id
                    Environment.Append("", Main.ClientMessage); // Controller
                    Environment.Append(Ammount, Main.ClientMessage); // Product Count
                    Environment.Append(Environment.GetTimestamp(), Main.ClientMessage);
                }
                Environment.Append(item.MinRank, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);

                if (item.Credits > 0)
                {
                    Main.Data.Credits -= item.Credits;
                    Environment.InitPacket(6, Main.ClientMessage);
                    Environment.Append(Integer.toString(Main.Data.Credits), Main.ClientMessage);
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                }

                if (item.ExtraCost == 4)
                {
                    Main.Data.Shells -= item.Pixels;
                    Environment.InitPacket(438, Main.ClientMessage);
                    Environment.Append(Main.Data.Shells, Main.ClientMessage);
                    Environment.Append(0, Main.ClientMessage);
                    Environment.Append(4, Main.ClientMessage); // Id Shells
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                }
                else if(item.Pixels > 0)
                {
                    Main.Data.Pixels -= item.Pixels;
                    Environment.InitPacket(438, Main.ClientMessage);
                    Environment.Append(Main.Data.Pixels, Main.ClientMessage);
                    Environment.Append(0, Main.ClientMessage);
                    Environment.Append(0, Main.ClientMessage); // Id Pixels
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                }

                Item ItemToBuy;

                DatabaseClient DB;
                try
                {
                    DB = new DatabaseClient(Environment.DataBase);
                }
                catch (Exception ex)
                {
                    Environment.Log.Print(ex);
                    return;
                }

                for(int e = 0;item.ItemsType.size() > e;e++)
                {
                    int SpriteId = item.ItemsSprite.get(e);
                    int Ammount = item.ItemsAmmount.get(e);
                    String Type = item.ItemsType.get(e);
                    
                    if (Item.Interaction == Environment.Furniture.DIMMER)
                    {
                        ExtraData = "1,1,1,#000000,255";
                    }
                    else if (Item.ItemName.toLowerCase().contains("prizetrophy"))
                    {
                        ExtraData = Main.Data.UserName + (char)9 + new Date() + (char)9 + ExtraData;
                    }

                    if(ExtraData.equals(""))
                    {
                        ExtraData = item.ItemsExtraData.get(e);
                    }
                    
                    if(IsDealBuy)
                    {
                        ItemToBuy = Environment.Furniture.IdsBySprite.get(SpriteId);
                    }
                    else
                    {
                        ItemToBuy = Item;
                    }

                    if( Type.equals("s") || Type.equals("i") )
                    {
                        List<Integer> Items = new ArrayList<Integer>();

                        for (int i = 0; i < Ammount; i++)
                        {
                            if (ItemToBuy.Interaction == Environment.Furniture.TELEPORT)
                            {
                                int Id1 = Environment.GenerateItemId();
                                Main.AddItem(Id1, "0", ItemToBuy);
                                int Id2 = Environment.GenerateItemId();
                                Main.AddItem(Id2, "0", ItemToBuy);
                                Environment.Teleports.SetParents(Id1,Id2);
                                continue;
                            }
                            else if (ItemToBuy.Interaction == Environment.Furniture.XMAS2010)
                            {
                                Main.GiveBadge("WD5");
                            }
                            else if (Environment.Furniture.WIRED_OTHER >= ItemToBuy.Interaction && ItemToBuy.Interaction >= Environment.Furniture.WIRED_CONDITION)
                            {
                                Main.GiveBadge("WIRD2");
                            }


                            int Id = Environment.GenerateItemId();
                            Main.AddItem(Id, ExtraData, ItemToBuy);
                            Items.add(Id);
                        }

                        // UnseenItems
                        Environment.InitPacket(832, Main.ClientMessage);
                        Environment.Append(1, Main.ClientMessage);
                        int NewType = Type.equals("i") ? 2 : 1;
                        Environment.Append(NewType, Main.ClientMessage);
                        Environment.Append(Ammount, Main.ClientMessage);
                        for(int NewItem : Items)
                        {
                            Environment.Append(NewItem, Main.ClientMessage);

                            UserItem UserItem = Main.GetItem(NewItem);

                            try
                            {
                                DB.SecureExec("INSERT INTO items (`id`,`base_item`,`extra_data`,`user_id`) VALUES (?,?,?,?);",
                                            Integer.toString(UserItem.Id),
                                            Integer.toString(UserItem.BaseItem.Id),
                                            UserItem.ExtraData,
                                            Integer.toString(Main.Data.Id)
                                            );
                            }
                            catch (Exception ex)
                            {
                                Environment.Log.Print(ex);
                            }
                        }
                        Environment.EndPacket(Main.Socket, Main.ClientMessage);
                        Items.clear();
                    }
                    else if(Type.equals("e"))
                    {
                        for (int j = 0; j < Ammount; j++)
                        {
                            int Time = 10;
                            Main.AddEffect(ItemToBuy.SpriteId, (Time * 60) + 1);
                        }
                    }
                }

                DB.Close();
            }
        }
        else
        {
            if (ItemId == 4896)
            {
                if(Main.Data.Credits < 15)
                {
                    Environment.InitPacket(68, Main.ClientMessage);
                    Environment.Append(true, Main.ClientMessage); // is credits problem
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                    return;
                }

                Environment.BuyVipOrClub(Main, 1, 1); // 1 = Club, 2 = Vip

                Main.Data.Credits -= 15;
                Environment.InitPacket(6, Main.ClientMessage);
                Environment.Append(Integer.toString(Main.Data.Credits), Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
            }
            else if (ItemId == 4897)
            {
                if (Main.Data.Credits < 25)
                {
                    Environment.InitPacket(68, Main.ClientMessage);
                    Environment.Append(true, Main.ClientMessage); // is credits problem
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                    return;
                }

                Environment.BuyVipOrClub(Main, 3, 1); // 1 = Club, 2 = Vip

                Main.Data.Credits -= 25;
                Environment.InitPacket(6, Main.ClientMessage);
                Environment.Append(Integer.toString(Main.Data.Credits), Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
            }
            else if (ItemId == 4898)
            {
                if (Main.Data.Credits < 45)
                {
                    Environment.InitPacket(68, Main.ClientMessage);
                    Environment.Append(true, Main.ClientMessage); // is credits problem
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                }

                Environment.BuyVipOrClub(Main, 1, 2); // 1 = Club, 2 = Vip

                Main.Data.Credits -= 45;
                Environment.InitPacket(6, Main.ClientMessage);
                Environment.Append(Integer.toString(Main.Data.Credits), Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
            }
            else if (ItemId == 4899)
            {
                if (Main.Data.Credits < 65)
                {
                    Environment.InitPacket(68, Main.ClientMessage);
                    Environment.Append(true, Main.ClientMessage); // is credits problem
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                    return;
                }

                Environment.BuyVipOrClub(Main, 3, 2); // 1 = Club, 2 = Vip

                Main.Data.Credits -= 65;
                Environment.InitPacket(6, Main.ClientMessage);
                Environment.Append(Integer.toString(Main.Data.Credits), Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
            }
        }
    }

    private boolean ValidPetNameChars(String inputStr,int HardCodedLen)
    {
        for(int i=0;i<HardCodedLen;i++)
        {
            if(i==0)
            {
                if(inputStr.charAt(0) == ' ')
                {
                    return false;
                }
            }
            if((inputStr.charAt(i) < 'a' || inputStr.charAt(i) > 'z') && (inputStr.charAt(i) < '0' || inputStr.charAt(i) > '9'))
            {
                return false;
            }
        }

        return true;
    }
}
