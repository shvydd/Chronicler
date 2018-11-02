package nodomain.shvydkoy.chronicler.api.webfeed.markup;

import java.util.ArrayList;
import java.util.List;



public final class Tag
{
    //Tag = <Name Attribute="AttributeValue"> Text </Name>
    private String Name; //
    private String FieldName;
    private ArrayList<Tag> Attributes;
    private boolean Multiple; //Multiple=true allows several identical tags inside same parent tag //CHECK IT IN MARKUPS
    private ArrayList<Tag> Nested;
    private Tag Parent;
    private int Depth;// Depth=1 corresponds to rootTag

    private boolean IsAttribute;
    private String Value; // If is IsAttribute, keeps attrbute value

    final public static String EXCEPTION_DESCRIPTION_IF_ILLEGAL_ARGUMENTS = "Tag Constructor: Name is null.";
    final public static String EXCEPTION_DESCRIPTION_IF_ATTRIBUTE_WITH_ATTRIBUTE = "Tag Class: Attempt to add attribute to attribute.";
    final public static String EXCEPTION_DESCRIPTION_IF_ATTRIBUTE_NESTED = "Tag Class: Attempt to add attribute Tag as nested Tag, or nesting Tag to an attribute Tag.";




    Tag(final String Name, boolean Multiple)
    {
        if (null == Name)
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ILLEGAL_ARGUMENTS);
        }
        this.Attributes = new ArrayList<>();
        this.Name = Name;
        this.Multiple = Multiple;
        this.Nested = new ArrayList<>();
        this.Parent = null;
        this.Depth = 1;

        this.IsAttribute = false;
        this.Value = null;

        this.FieldName = null;

    }

    Tag(final String Name)
    {
        if (null == Name)
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ILLEGAL_ARGUMENTS);
        }
        this.Attributes = new ArrayList<>();
        this.Name = Name;
        this.Multiple = false;
        this.Nested = new ArrayList<>();
        this.Parent = null;
        this.Depth = 1;

        this.IsAttribute = false;
        this.Value = null;

        this.FieldName = null;
    }



    Tag(final String Name, Tag Holder)
    {
        if (null == Name)
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ILLEGAL_ARGUMENTS);
        }
        if (Holder.IsAttribute)
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ATTRIBUTE_WITH_ATTRIBUTE);
        }

        this.Name = Name;
        this.Value = null;
        IsAttribute = true;
        this.Depth = Holder.Depth;

        this.Attributes = null;
        this.Multiple = false;
        this.Nested = null;
        this.Parent = Holder;

        Holder.Attributes.add(this);

        this.FieldName = null;
    }

    Tag(final String Name, final String Value, Tag Holder)
    {
        if (null == Name)
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ILLEGAL_ARGUMENTS);
        }
        if (Holder.IsAttribute)
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ATTRIBUTE_WITH_ATTRIBUTE);
        }

        this.Name = Name;
        this.Value = Value;
        IsAttribute = true;
        this.Depth = Holder.Depth;

        this.Attributes = null;
        this.Multiple = false;
        this.Nested = null;
        this.Parent = Holder;

        Holder.Attributes.add(this);

        this.FieldName = null;
    }



    final void nest(Tag tagToNest)
    {
        if(tagToNest.IsAttribute || this.IsAttribute)
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ATTRIBUTE_NESTED);
        }


        if (!tagToNest.equals(this))
        {
            tagToNest.Parent = this;
            tagToNest.Depth = this.Depth + 1;
            this.Nested.add(tagToNest);
        }
    }

    final void addAttribute(final String Name, final String Value)
    {
        if (IsAttribute)
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ATTRIBUTE_WITH_ATTRIBUTE);
        }
        new Tag(Name, Value, this);
    }

    final void addAttribute(String Name)
    {
        if (IsAttribute)
        {
            throw new IllegalArgumentException(EXCEPTION_DESCRIPTION_IF_ATTRIBUTE_WITH_ATTRIBUTE);
        }
        new Tag(Name,this);
    }







    final public String getName()
    {
        return Name;
    }

    final public List<Tag> getAttributes()
    {
        return Attributes;
    }

    final public boolean isMultiple()
    {
        return Multiple;
    }

    final public List<Tag> getNested()
    {
        return Nested;
    }

    final public Tag getParent()
    {
        if (!IsAttribute)
        {
            return Parent;
        }
        else
        {
            return null;
        }
    }

    final public int getDepth()
    {
        return Depth;
    }

    final public boolean isAttribute()
    {
        return IsAttribute;
    }

    final public Tag getHolder()
    {
        if (IsAttribute)
        {
            return Parent;
        }
        else
        {
            return null;
        }
    }

    final void setFieldName(final String fieldName)
    {
        FieldName = fieldName;
    }

    final public String getFieldName()
    {
        return FieldName;
    }
}
